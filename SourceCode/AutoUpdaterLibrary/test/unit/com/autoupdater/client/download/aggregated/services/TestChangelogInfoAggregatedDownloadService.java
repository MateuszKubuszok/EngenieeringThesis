package com.autoupdater.client.download.aggregated.services;

import static org.fest.assertions.api.Assertions.assertThat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.SortedSet;

import org.junit.Test;

import com.autoupdater.client.download.DownloadResultException;
import com.autoupdater.client.download.runnables.HttpURLConnectionMock;
import com.autoupdater.client.download.services.BugsInfoDownloadService;
import com.autoupdater.client.models.Program;
import com.autoupdater.client.models.ProgramBuilder;

public class TestChangelogInfoAggregatedDownloadService {
    @Test
    public void testService() throws MalformedURLException {
        // given
        BugsInfoAggregatedDownloadService aggregatedService = new BugsInfoAggregatedDownloadService();

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + ("<bugs>" + ("<bug>Some bug description</bug>")
                        + ("<bug>Some other description</bug>") + "</bugs>");
        aggregatedService.addService(new BugsInfoDownloadService(new HttpURLConnectionMock(new URL(
                "http://127.0.0.1"), xml)), ProgramBuilder.builder().setName("Program")
                .setPathToProgramDirectory("/").setServerAddress("127.0.0.1").build());

        xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + ("<bugs>" + ("<bug>Some bug description 2</bug>")
                        + ("<bug>Some other description 2</bug>") + "</bugs>");
        aggregatedService.addService(new BugsInfoDownloadService(new HttpURLConnectionMock(new URL(
                "http://127.0.0.1"), xml)), ProgramBuilder.builder().setName("Program 2")
                .setPathToProgramDirectory("/").setServerAddress("127.0.0.1").build());

        SortedSet<Program> result = null;
        boolean exceptionThrown = false;

        // when
        aggregatedService.start();
        aggregatedService.joinThread();
        try {
            result = aggregatedService.getResult();
        } catch (DownloadResultException e) {
            exceptionThrown = true;
        }

        // then
        assertThat(exceptionThrown).as(
                "getResult() should not throw exception when result is ready, and without errors")
                .isFalse();
        assertThat(result).as("getResult() should aggregate results from all services").isNotNull()
                .hasSize(2);
    }
}
