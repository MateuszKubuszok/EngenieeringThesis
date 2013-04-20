package com.autoupdater.client.download.aggregated.services;

import static org.fest.assertions.api.Assertions.assertThat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.SortedSet;

import org.junit.Test;

import com.autoupdater.client.download.DownloadResultException;
import com.autoupdater.client.download.runnables.HttpURLConnectionMock;
import com.autoupdater.client.download.services.ChangelogInfoDownloadService;
import com.autoupdater.client.models.Package;
import com.autoupdater.client.models.PackageBuilder;

public class TestBugsInfoAggregatedDownloadService {
    @Test
    public void testService() throws MalformedURLException {
        // given
        ChangelogInfoAggregatedDownloadService aggregatedService = new ChangelogInfoAggregatedDownloadService();

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + ("<package>"
                        + ("<changelogs>" + ("<changelog>Initial release</changelog>")
                                + ("<version>1</version>") + "</changelogs>")
                        + ("<changelogs>" + ("<changelog>Update</changelog>")
                                + "<version>1.5.0.0</version>" + "</changelogs>") + "</package>");
        aggregatedService.addService(new ChangelogInfoDownloadService(new HttpURLConnectionMock(
                new URL("http://127.0.0.1"), xml)), PackageBuilder.builder().setName("1")
                .setID("1").build());

        xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + ("<package>"
                        + ("<changelogs>" + ("<changelog>Other release</changelog>")
                                + ("<version>2</version>") + "</changelogs>")
                        + ("<changelogs>" + ("<changelog>Update</changelog>")
                                + "<version>2.0.0.0</version>" + "</changelogs>") + "</package>");
        aggregatedService.addService(new ChangelogInfoDownloadService(new HttpURLConnectionMock(
                new URL("http://127.0.0.1"), xml)), PackageBuilder.builder().setName("2")
                .setID("2").build());

        SortedSet<Package> result = null;
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
