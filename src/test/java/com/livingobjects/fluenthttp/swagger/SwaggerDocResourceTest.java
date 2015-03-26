package com.livingobjects.fluenthttp.swagger;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import net.codestory.http.WebServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class SwaggerDocResourceTest {

    private static WebServer webServer;


    @BeforeClass
    public static void setUp() throws Exception {
        ImmutableList<File> swaggerFiles = ImmutableList.of(new File(SwaggerDocResourceTest.class.getClassLoader().getResource("api.yaml").toURI()));
        webServer = new WebServer().configure(routes -> SwaggerEntryPoint.addApiDocRoutes(routes, swaggerFiles)).startOnRandomPort();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        webServer.stop();
    }


    @Test
    public void shouldDisplayIndexPage() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:" + webServer.port() + "/api-doc")
                .build();

        Response response = new OkHttpClient().newCall(request).execute();

        assertThat(response.body().string()).contains("<a href=\"api-doc/translations\">translations</a>");
    }

    @Test
    public void shouldDisplayTranslationsPage() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:" + webServer.port() + "/api-doc/translations")
                .build();

        Response response = new OkHttpClient().newCall(request).execute();

        assertThat(response.body().string()).isEqualTo(Files.toString(new File(SwaggerDocResourceTest.class.getClassLoader().getResource("expectedTranslationsPage.html").toURI()), Charsets.UTF_8));
    }

    @Test
    public void shouldDisplayRawYamlApi() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:" + webServer.port() + "/api-doc/translations/raw")
                .build();

        Response response = new OkHttpClient().newCall(request).execute();

        assertThat(response.body().string()).isEqualTo(Files.toString(new File(SwaggerDocResourceTest.class.getClassLoader().getResource("api.yaml").toURI()), Charsets.UTF_8));
    }

    @Test
    @Ignore
    public void infiniteServer() throws Exception {
        webServer.start(9200);

        while (true) {
            Thread.sleep(1000);
        }
    }
}