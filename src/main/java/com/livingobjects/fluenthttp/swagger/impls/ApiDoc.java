package com.livingobjects.fluenthttp.swagger.impls;

import com.google.common.collect.ImmutableSet;
import com.livingobjects.myrddin.ApiSpecification;

import java.io.File;

public final class ApiDoc {

    public final ImmutableSet<String> baseUris;

    public final File swaggerFile;

    public final ApiSpecification apiSpecification;

    public ApiDoc(ImmutableSet<String> baseUris, File swaggerFile, ApiSpecification apiSpecification) {
        this.baseUris = baseUris;
        this.swaggerFile = swaggerFile;
        this.apiSpecification = apiSpecification;
    }
}
