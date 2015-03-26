package com.livingobjects.fluenthttp.swagger.impls;

import com.google.common.collect.ImmutableSet;
import com.livingobjects.myrddin.ApiSpecification;

import java.net.URL;

public final class ApiDoc {

    public final ImmutableSet<String> baseUris;

    public final URL swaggerUrl;

    public final ApiSpecification apiSpecification;

    public ApiDoc(ImmutableSet<String> baseUris, URL swaggerUrl, ApiSpecification apiSpecification) {
        this.baseUris = baseUris;
        this.swaggerUrl = swaggerUrl;
        this.apiSpecification = apiSpecification;
    }
}
