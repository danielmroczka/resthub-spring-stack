package org.resthub.web.controller;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.fest.assertions.api.Assertions;
import org.resthub.test.common.AbstractWebTest;
import org.resthub.web.Client;
import org.resthub.web.Http;
import org.resthub.web.model.Sample;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class JsonServiceBasedRestControllerTest extends AbstractWebTest {

    protected String rootUrl() {
        return "http://localhost:" + port + "/service-based";
    }

    @AfterMethod
    public void tearDown() {
        try {
            Client.url(rootUrl()).delete().get();
        } catch (InterruptedException | ExecutionException e) {
            Assertions.fail("Exception during delete all request", e);
        }
    }

    @Test
    public void testCreateResource() throws IllegalArgumentException, InterruptedException, ExecutionException,
            IOException {
        Sample r = new Sample("toto");
        Client.Response response = Client.url(rootUrl()).jsonPost(r).get();
        r = (Sample) response.jsonDeserialize(r.getClass());
        Assertions.assertThat(r).isNotNull();
        Assertions.assertThat(r.getName()).isEqualTo("toto");
    }

    @Test
    public void testFindAllResources() throws IllegalArgumentException, InterruptedException, ExecutionException,
            IOException {
        Client.url(rootUrl()).jsonPost(new Sample("toto")).get();
        Client.url(rootUrl()).jsonPost(new Sample("toto")).get();
        String responseBody = Client.url(rootUrl()).getJson().get().getBody();
        Assertions.assertThat(responseBody).contains("toto");
    }

    @Test
    public void testPagingFindAllResources() throws IllegalArgumentException, InterruptedException, ExecutionException,
            IOException {
        Client.url(rootUrl()).jsonPost(new Sample("toto")).get();
        Client.url(rootUrl()).jsonPost(new Sample("toto")).get();
        String responseBody = Client.url(rootUrl() + "/page/0").getJson().get().getBody();
        Assertions.assertThat(responseBody).contains("\"totalElements\":2");
    }

    @Test
    public void testDeleteResource() throws IllegalArgumentException, IOException, InterruptedException,
            ExecutionException {
        Sample r = new Sample("toto");
        r = (Sample) Client.url(rootUrl()).jsonPost(r).get().jsonDeserialize(r.getClass());
        Assertions.assertThat(r).isNotNull();

        Client.Response response = Client.url(rootUrl() + "/" + r.getId()).delete().get();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.NO_CONTENT);

        response = Client.url(rootUrl() + "/" + r.getId()).get().get();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.NOT_FOUND);
    }

    @Test
    public void testFindResource() throws IllegalArgumentException, IOException, InterruptedException,
            ExecutionException {
        Sample r = new Sample("toto");
        r = (Sample) Client.url(rootUrl()).jsonPost(r).get().jsonDeserialize(r.getClass());

        Client.Response response = Client.url(rootUrl() + "/" + r.getId()).get().get();
        Assertions.assertThat(response.getStatus()).isEqualTo(Http.OK);
    }

    @Test
    public void testUpdate() throws IllegalArgumentException, IOException, InterruptedException, ExecutionException {
        Sample r1 = new Sample("toto");
        r1 = Client.url(rootUrl()).jsonPost(r1).get().jsonDeserialize(r1.getClass());

        Sample r2 = new Sample(r1);
        r2.setName("titi");
        r2 = Client.url(rootUrl() + "/" + r1.getId()).jsonPut(r2).get().jsonDeserialize(r2.getClass());

        Assertions.assertThat(r1).isNotEqualTo(r2);
        Assertions.assertThat(r1.getName()).contains("toto");
        Assertions.assertThat(r2.getName()).contains("titi");
    }
}
