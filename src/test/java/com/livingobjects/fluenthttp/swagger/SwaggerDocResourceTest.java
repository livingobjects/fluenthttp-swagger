package com.livingobjects.fluenthttp.swagger;

import com.google.common.collect.ImmutableList;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import net.codestory.http.WebServer;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class SwaggerDocResourceTest {


    @Test
    public void shouldWork() throws Exception {
        ImmutableList<File> of = ImmutableList.of(new File(SwaggerDocResourceTest.class.getClassLoader().getResource("api.yaml").toURI()));
        WebServer webServer = new WebServer().configure(routes -> routes.add(new SwaggerDocResource(of))
        ).startOnRandomPort();

        Request request = new Request.Builder()
                .url("http://localhost:" + webServer.port() + "/api-doc")
                .build();

        Response response = new OkHttpClient().newCall(request).execute();

        assertThat(response.body().string()).contains("<a href=\"api-doc/translations\">translations</a>");

        webServer.stop();
    }

    @Test
    public void infiniteServer() throws Exception {
        ImmutableList<File> of = ImmutableList.of(new File(SwaggerDocResourceTest.class.getClassLoader().getResource("api.yaml").toURI()));
        WebServer webServer = new WebServer().configure(routes -> routes.add(new SwaggerDocResource(of))
        ).startOnRandomPort();

        System.out.println(webServer.port());

        while (true) {
            Thread.sleep(1000);
        }
    }
}