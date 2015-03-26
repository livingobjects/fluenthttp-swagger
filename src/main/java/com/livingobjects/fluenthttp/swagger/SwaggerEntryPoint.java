package com.livingobjects.fluenthttp.swagger;

import com.google.common.collect.ImmutableList;
import net.codestory.http.compilers.CompilersConfiguration;
import net.codestory.http.extensions.Extensions;
import net.codestory.http.misc.Env;
import net.codestory.http.routes.Routes;

import java.net.URL;

public class SwaggerEntryPoint {

    public static Routes addApiDocRoutes(Routes routes, ImmutableList<URL> swaggerFiles) {
        return routes.add(new SwaggerDocResource(swaggerFiles))
                .setExtensions(new Extensions() {
                    @Override
                    public void configureCompilers(CompilersConfiguration compilers, Env env) {
                        compilers.configureHandlebars(handlebar -> handlebar.infiniteLoops(true));
                    }
                });
    }
}
