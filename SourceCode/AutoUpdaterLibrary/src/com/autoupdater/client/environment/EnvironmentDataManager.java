package com.autoupdater.client.environment;

import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

import com.autoupdater.client.environment.settings.ClientSettingsBuilder;
import com.autoupdater.client.environment.settings.ProgramSettings;
import com.autoupdater.client.xml.creators.ConfigurationXMLCreator;
import com.autoupdater.client.xml.creators.InstallationDataXMLCreator;
import com.autoupdater.client.xml.parsers.ConfigurationParser;
import com.autoupdater.client.xml.parsers.InstallationDataParser;
import com.autoupdater.client.xml.parsers.ParserException;

/**
 * Manages EnvironmentData - in particular, saves and stores/reads settings and
 * installation data into/from files. Also allows to create default
 * EnvironmentData.
 * 
 * <p>
 * Uses EnvironmentContext as:
 * <ul>
 * <li>a source of default settings,</li>
 * <li>a way to find location of client's setting/installation data.</li>
 * </ul>
 * </p>
 * 
 * @see com.autoupdater.client.environment.EnvironmentData
 */
public class EnvironmentDataManager {
    private final EnvironmentContext environmentContext;

    /**
     * Creates instance of EnvironmentDataManager.
     */
    public EnvironmentDataManager() {
        environmentContext = new EnvironmentContext();
    }

    /**
     * Creates instance of EnvironmentDataManager with custom
     * EnvironmentContext.
     * 
     * @param environmentContext
     *            context describing default options
     */
    public EnvironmentDataManager(EnvironmentContext environmentContext) {
        this.environmentContext = environmentContext;
    }

    /**
     * Returns currently used EnvironmentContext.
     * 
     * @return current EnvironmentContext
     */
    public EnvironmentContext getEnviromentContext() {
        return environmentContext;
    }

    /**
     * Returns EnvironmentData, that can be used to initiate Client instance.
     * 
     * <p>
     * Reads settings and installation data from files described in
     * EnvironmentContext.
     * </p>
     * 
     * @return new EnvironmentData
     * @throws ClientEnvironmentException
     *             thrown if settings/installation data cannot be parsed
     * @throws IOException
     *             thrown if file with settings/installation data cannot be read
     */
    public EnvironmentData getEnvironmentData() throws ClientEnvironmentException, IOException {
        try {
            File settingsXMLFile = new File(environmentContext.getSettingsXMLPath());
            if (!settingsXMLFile.exists() || !settingsXMLFile.canRead())
                throw new IOException("File does not exists or cannot be read");

            EnvironmentData environmentData = new ConfigurationParser().parseXML(settingsXMLFile);

            File installationDataXMLFile = new File(environmentContext.getInstallationDataXMLPath());
            if (installationDataXMLFile.exists() && installationDataXMLFile.canRead())
                environmentData.setInstallationsData(new InstallationDataParser()
                        .parseXML(installationDataXMLFile));

            return environmentData.setEnvironmentDataManager(this);
        } catch (ParserException e) {
            throw new ClientEnvironmentException(e.getMessage());
        }
    }

    /**
     * Saves EnvironmentData into files described in EnvironmentContext.
     * 
     * @param environmentData
     *            EnvironmentData intended to save
     * @throws IOException
     *             thrown if settings cannot be written to a file
     */
    public void setEnvironmentData(EnvironmentData environmentData) throws IOException {
        new ConfigurationXMLCreator().createXML(new File(environmentContext.getSettingsXMLPath()),
                environmentData.getClientSettings(), environmentData.getProgramsSettings());

        new InstallationDataXMLCreator().createXML(
                new File(environmentContext.getInstallationDataXMLPath()),
                environmentData.getInstallationsData());
    }

    /**
     * Creates EnvironmentData with settings defined as default in
     * EnvironmetnContext (without proxy).
     * 
     * @see #createDefaultSettingsWithProxy()
     * 
     * @return new EnvironmentData
     */
    public EnvironmentData createDefaultSettings() {
        return new EnvironmentData(ClientSettingsBuilder.builder()
                .setClientName(environmentContext.getDefaultClientName())
                .setClientExecutableName(environmentContext.getDefaultClientExecutable())
                .setPathToClient(environmentContext.getDefaultPathToClient())
                .setPathToClientDirectory(environmentContext.getDefaultPathToClientDirectory())
                .setPathToInstaller(environmentContext.getDefaultPathToInstaller())
                .setPathToUACHandler(environmentContext.getDefaultPathToUACHandler()).build(),
                new TreeSet<ProgramSettings>()).setEnvironmentDataManager(this);
    }

    /**
     * Creates EnvironmentData with settings defined as default in
     * EnvironmetnContext (with proxy).
     * 
     * @see #createDefaultSettings()
     * 
     * @return new EnvironmentData
     */
    public EnvironmentData createDefaultSettingsWithProxy() {
        return new EnvironmentData(ClientSettingsBuilder.builder()
                .setClientName(environmentContext.getDefaultClientName())
                .setClientExecutableName(environmentContext.getDefaultClientExecutable())
                .setPathToClient(environmentContext.getDefaultPathToClient())
                .setPathToClientDirectory(environmentContext.getDefaultPathToClientDirectory())
                .setPathToInstaller(environmentContext.getDefaultPathToInstaller())
                .setPathToUACHandler(environmentContext.getDefaultPathToUACHandler())
                .setProxyAddress(environmentContext.getDefaultProxyAddress())
                .setProxyPort(environmentContext.getDefaultProxyPort()).build(),
                new TreeSet<ProgramSettings>()).setEnvironmentDataManager(this);
    }
}
