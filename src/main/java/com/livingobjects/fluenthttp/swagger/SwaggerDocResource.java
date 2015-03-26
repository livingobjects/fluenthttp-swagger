/*
 * #%L
 * Fluenthttp-Swagger
 * %%
 * Copyright (C) 2014 LivingObjects SAS
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.livingobjects.fluenthttp.swagger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.livingobjects.fluenthttp.swagger.impls.ApiDoc;
import com.livingobjects.myrddin.ApiSpecification;
import com.livingobjects.myrddin.Wizard;
import com.livingobjects.myrddin.exception.SwaggerException;
import net.codestory.http.annotations.Get;
import net.codestory.http.payload.Payload;
import net.codestory.http.templating.ModelAndView;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static net.codestory.http.payload.Payload.notFound;


public final class SwaggerDocResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerDocResource.class);

    private final ConcurrentHashMap<String, ApiDoc> baseUriMap = new ConcurrentHashMap<>();

    public SwaggerDocResource(ImmutableList<File> swaggerFiles) {
        for (File swaggerFile : swaggerFiles) {
            try (InputStream in = Files.newInputStream(swaggerFile.toPath())) {
                if (in != null) {
                    Wizard wizard = new Wizard();
                    try {
                        ApiSpecification apiSpecification = wizard.generateSpecification(in);
                        Set<String> baseUris = apiSpecification.resources.stream().map(r -> {
                            String[] uris = r.uri.split("/");
                            if (uris.length > 2) {
                                return uris[1];
                            } else {
                                return null;
                            }
                        }).filter(s -> s != null).collect(Collectors.toSet());
                        ImmutableSet<String> immutableBaseUris = ImmutableSet.copyOf(baseUris);
                        ApiDoc apiDoc = new ApiDoc(immutableBaseUris, swaggerFile, apiSpecification);
                        for (String baseUri : immutableBaseUris) {
                            baseUriMap.put(baseUri, apiDoc);
                        }
                    } catch (SwaggerException e) {
                        LOGGER.error("Swagger documentation '{}' is invalid.", swaggerFile, e);
                    }
                } else {
                    LOGGER.error("Swagger documentation '{}' not found. Check Swagger-Doc attribute in manifest.", swaggerFile);
                }
            } catch (IOException e) {
                LOGGER.error("Swagger documentation '{}' not found. Check Swagger-Doc attribute in manifest.", swaggerFile, e);
            }
        }
    }

    @Get("/api-doc")
    public ModelAndView displayDocumentationIndex() {
        return ModelAndView.of("swagger-index", "uris", baseUriMap.keySet());
    }

    @Get("/api-doc/:api")
    public Payload displayDocumentation(String api) {
        ApiDoc apiDoc = baseUriMap.get(api);
        if (apiDoc != null) {
            return new Payload(ModelAndView.of("swagger-doc", "apiBaseUri", api, "api", apiDoc.apiSpecification));
        } else {
            return notFound();
        }
    }

    @Get("/api-doc/:api/raw")
    public Payload displayRawDocumentation(String api) {
        ApiDoc apiDoc = baseUriMap.get(api);
        if (apiDoc != null) {
            URL url;
            try {
                url = apiDoc.swaggerFile.toURI().toURL();
            } catch (MalformedURLException e) {
                LOGGER.error("Swagger documentation '{}' not found", apiDoc.swaggerFile);
                return notFound();
            }
            try (InputStream in = url.openStream()) {
                return new Payload("text/plain", IOUtils.toString(in));
            } catch (IOException e) {
                LOGGER.error("Swagger documentation '{}' not found", apiDoc.swaggerFile, e);
                return notFound();
            }
        } else {
            return notFound();
        }
    }
}
