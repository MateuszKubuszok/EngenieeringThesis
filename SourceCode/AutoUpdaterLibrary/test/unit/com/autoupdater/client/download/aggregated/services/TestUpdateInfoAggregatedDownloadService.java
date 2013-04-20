package com.autoupdater.client.download.aggregated.services;

import static org.fest.assertions.api.Assertions.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

import com.autoupdater.client.download.DownloadResultException;
import com.autoupdater.client.download.runnables.HttpURLConnectionMock;
import com.autoupdater.client.download.services.UpdateInfoDownloadService;
import com.autoupdater.client.models.Package;
import com.autoupdater.client.models.PackageBuilder;
import com.autoupdater.client.models.Program;
import com.autoupdater.client.models.ProgramBuilder;
import com.autoupdater.client.models.Update;

public class TestUpdateInfoAggregatedDownloadService {
    @Test
    public void testService() {
        try {
            // given
            UpdateInfoAggregatedDownloadService aggregatedService = new UpdateInfoAggregatedDownloadService();

            Program program = ProgramBuilder
                    .builder()
                    .setName("name")
                    .setPackages(
                            new TreeSet<Package>(Arrays.asList(PackageBuilder.builder()
                                    .setName("1").setID("1").build(), PackageBuilder.builder()
                                    .setName("2").setID("2").build()))).build();
            program.setDevelopmentVersion(true);

            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                    + ("<updates>"
                            + ("<update packageName=\"Test package\" packageId=\"2\" "
                                    + "version=\"12.34.56.78\" developmentVersion=\"true\" "
                                    + "id=\"1\" type=\"copy\" originalName=\"file.ini\" relativePath=\"/\">Some changes</update>")
                            + "</updates>");
            aggregatedService.addService(new UpdateInfoDownloadService(new HttpURLConnectionMock(
                    new URL("http://127.0.0.1"), xml)), program.getPackages().first());

            xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                    + ("<updates>"
                            + ("<update packageName=\"other test package\" packageId=\"3\" "
                                    + "version=\"12.34.56.78\" developmentVersion=\"true\" "
                                    + "id=\"4\" type=\"copy\" originalName=\"file.ini\" relativePath=\"/\">Some other changes</update>")
                            + "</updates>");
            aggregatedService.addService(new UpdateInfoDownloadService(new HttpURLConnectionMock(
                    new URL("http://127.0.0.1"), xml)), program.getPackages().last());

            SortedSet<Update> result = null;

            // when
            aggregatedService.start();
            aggregatedService.joinThread();
            result = aggregatedService.getResult();

            // then
            assertThat(result).as("getResult() should aggregate results from all services")
                    .isNotNull().hasSize(2);
        } catch (DownloadResultException | MalformedURLException e) {
            fail("getResult() should not throw exception when result is ready, and without errors");
        }
    }
}
