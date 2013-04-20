package com.autoupdater.client.environment;

import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

import com.autoupdater.client.environment.settings.ClientSettings;
import com.autoupdater.client.environment.settings.ProgramSettings;
import com.autoupdater.client.models.Package;
import com.autoupdater.client.models.Program;
import com.autoupdater.client.models.ProgramBuilder;
import com.autoupdater.client.models.Update;
import com.autoupdater.system.EOperatingSystem;
import com.google.common.base.Objects;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

/**
 * Class containing all information used by Client. It is used as a source of
 * information by virtually all classes that require Client's or Programs'
 * settings as well as information about installed Programs and Packages.
 * 
 * <p>
 * Should be created and saved only by EnvironmentDataManager.
 * </p>
 * 
 * @see com.autoupdater.client.environment.EnvironmentDataManager
 * @see com.autoupdater.client.environment.settings.ClientSettings
 * @see com.autoupdater.client.environment.settings.ProgramSettings
 * @see com.autoupdater.client.environment.AvailabilityFilter
 * @see com.autoupdater.client.models.Program
 */
public class EnvironmentData {
    private EnvironmentDataManager environmentDataManager;
    private final EOperatingSystem system;
    private final ClientSettings clientSettings;
    private final SortedSet<ProgramSettings> programsSettings;
    private SortedSet<Program> installationsData;

    /**
     * Creates EnvironmentData instance.
     * 
     * @param clientSettings
     *            ClientSettins instance
     * @param programsSettings
     *            ProgramSettings instance
     */
    public EnvironmentData(ClientSettings clientSettings,
            SortedSet<ProgramSettings> programsSettings) {
        this.clientSettings = clientSettings;
        this.programsSettings = programsSettings;
        this.system = EOperatingSystem.current();
        setInstallationsData(null);
    }

    /**
     * Sets EnvironmentDataManager.
     * 
     * <p>
     * Should be used only by EnvironmentDataManager that created
     * EnvironmentData.
     * </p>
     * 
     * @param environmentDataManager
     *            parent of this object
     * @return this object (used for chaining)
     */
    EnvironmentData setEnvironmentDataManager(EnvironmentDataManager environmentDataManager) {
        this.environmentDataManager = environmentDataManager;
        return this;
    }

    /**
     * Saves current state of EnvironmentData.
     * 
     * <p>
     * Particularly useful when saving current state if installation data
     * changed (e.g. when some update was successfully installed).
     * </p>
     * 
     * @throws IOException
     *             thrown if some settings couldn't be saved
     * @throws ClientEnvironmentException
     *             thrown if EnvironmentDataManager wasn't set in constructor,
     *             yet setting were tried to be saved
     */
    public void save() throws IOException, ClientEnvironmentException {
        if (environmentDataManager == null)
            throw new ClientEnvironmentException("EnvironmentDataManager was not set");
        environmentDataManager.setEnvironmentData(this);
    }

    /**
     * Returns ClientSettings currently used by EnvironmentData.
     * 
     * @return ClientSettings instance
     */
    public ClientSettings getClientSettings() {
        return clientSettings;
    }

    /**
     * Returns set of ProgramSettings currently defined in EnvironmentData.
     * 
     * @return ProgramSettings set
     */
    public SortedSet<ProgramSettings> getProgramsSettings() {
        return programsSettings;
    }

    /**
     * Gets information about installations.
     * 
     * <p>
     * Installations data contains information about all programs that are
     * considered updatable - which means it will contains all of Programs that
     * have ProgramSettings defined, and only those.
     * </p>
     * 
     * <p>
     * Programs that are actually installed but doesn't have their
     * ProgramSettings will be ignored.
     * </p>
     * 
     * <p>
     * Programs that are not installed according to installation data, but have
     * </p>
     * 
     * @return installed programs
     */
    public SortedSet<Program> getInstallationsData() {
        return installationsData;
    }

    /**
     * Sets set of Programs that are installed.
     * 
     * <p>
     * Passed data will be complimented with information from ProgramsSettings.
     * Also all programs that do not have ProgramSettings will be removed.
     * </p>
     * 
     * @see #complimentInstallationsDataWithInstalledProgramsWithSettings(SortedSet,
     *      SortedSet)
     * @see #complimentInstallationsDataWithNotInstalledButDefinedBySettings(SortedSet,
     *      SortedSet)
     * 
     * @param installationsData
     */
    void setInstallationsData(SortedSet<Program> installationsData) {
        (this.installationsData = this.installationsData == null ? new TreeSet<Program>()
                : this.installationsData).clear();
        installationsData = installationsData == null ? new TreeSet<Program>() : installationsData;

        SortedSet<Program> programsDefinedBySettings = getMockProgramsDefinedBySettings();

        complimentInstallationsDataWithInstalledProgramsWithSettings(programsDefinedBySettings,
                installationsData);
        complimentInstallationsDataWithNotInstalledButDefinedBySettings(programsDefinedBySettings,
                installationsData);
    }

    /**
     * Returns used Operating System.
     * 
     * @return operating system
     */
    public EOperatingSystem getSystem() {
        return system;
    }

    /**
     * Returns AvailabilityFilter for this EnvironmentData.
     * 
     * @return AvailabilityFilter instance
     */
    public AvailabilityFilter getAvailabilityFilter() {
        return new AvailabilityFilter(this);
    }

    /**
     * Returns list containing one ProgramSettings instance for each server.
     * Used for listing repositories (each server would be queried only once).
     * 
     * @return list of ProgramSettings with unique server address'
     */
    public SortedSet<ProgramSettings> getProgramsSettingsForEachServer() {
        SortedSet<String> alreadyFoundServers = new TreeSet<String>();
        SortedSet<ProgramSettings> programsSettings = new TreeSet<ProgramSettings>();

        for (ProgramSettings programSettings : this.programsSettings) {
            String serverAddress = programSettings.getServerAddress();

            if (serverAddress != null && !alreadyFoundServers.contains(serverAddress)) {
                programsSettings.add(programSettings);
                alreadyFoundServers.add(serverAddress);
            }
        }

        return programsSettings;
    }

    /**
     * Returns ProgramSettings for Program.
     * 
     * @param program
     *            Program for which ProgramSettings will be searched
     * @return ProgramSettings instance
     * @throws ProgramSettingsNotFoundException
     *             thrown if ProgramSettings couldn't be found
     */
    public ProgramSettings findProgramSettingsForProgram(Program program)
            throws ProgramSettingsNotFoundException {
        if (program != null)
            for (ProgramSettings programSettings : programsSettings) {
                if (Objects.equal(programSettings.getProgramName(), program.getName())
                        && Objects.equal(programSettings.getPathToProgramDirectory(),
                                program.getPathToProgramDirectory())
                        && Objects.equal(programSettings.getServerAddress(),
                                program.getServerAddress()))
                    return programSettings;
            }
        throw new ProgramSettingsNotFoundException("Could not find settings for program: "
                + program);
    }

    /**
     * Returns ProgramSettings for Program by Package belonging to it.
     * 
     * @param _package
     *            Package for which ProgramSettings will be searched
     * @return ProgramSettings instance
     * @throws ProgramSettingsNotFoundException
     *             thrown if ProgramSettings couldn't be found
     */
    public ProgramSettings findProgramSettingsForPackage(Package _package)
            throws ProgramSettingsNotFoundException {
        if (_package != null)
            return findProgramSettingsForProgram(_package.getProgram());
        throw new ProgramSettingsNotFoundException("Could not find settings for package: "
                + _package);
    }

    /**
     * Returns ProgramSettings for Program by Update belonging to it.
     * 
     * @param update
     *            Update for which ProgramSettings will be searched
     * @return ProgramSettings instance
     * @throws ProgramSettingsNotFoundException
     *             thrown if ProgramSettings couldn't be found
     */
    public ProgramSettings findProgramSettingsForUpdate(Update update)
            throws ProgramSettingsNotFoundException {
        if (update != null)
            return findProgramSettingsForPackage(update.getPackage());
        throw new ProgramSettingsNotFoundException("Could not find settings for update: " + update);
    }

    /**
     * Returns set of Programs created from ProgramsSettings. Can be used both
     * as base of defined but not installed programs and to comparison with
     * actual installed programs (e.g. to check existence of such installation).
     * 
     * <p>
     * Created mocks have filled all important fields:
     * <ul>
     * <li>program's name,</li>
     * <li>program's directory,</li>
     * <li>repository's address,</li>
     * <li>whether or not program is development version.</li>
     * </ul>
     * </p>
     * 
     * @return set of Programs
     */
    public SortedSet<Program> getMockProgramsDefinedBySettings() {
        SortedSet<Program> programsDefinedBySettings = new TreeSet<Program>();
        if (programsSettings != null)
            for (ProgramSettings programSettings : programsSettings)
                programsDefinedBySettings.add(ProgramBuilder.builder()
                        .setName(programSettings.getProgramName())
                        .setPathToProgramDirectory(programSettings.getPathToProgramDirectory())
                        .setServerAddress(programSettings.getServerAddress())
                        .setDevelopmentVersion(programSettings.isDevelopmentVersion()).build());

        return programsDefinedBySettings;
    }

    /**
     * Compliments installation data passed into EnvironmentData with installed
     * Programs (according to passed parameter) that have defined settings.
     * 
     * <p>
     * Ensures that such programs are present even if they are not installed
     * (they will be listed as installed with no Packages).
     * </p>
     * 
     * <p>
     * Programs are also automatically filled with information about whether
     * they use development version.
     * </p>
     * 
     * @see #setInstallationsData(SortedSet)
     * @see #complimentInstallationsDataWithNotInstalledButDefinedBySettings(SortedSet,
     *      SortedSet)
     * 
     * @param programsDefinedBySettings
     *            set of Programs present in settings definitions
     * @param programsDefinedByInstallationsData
     *            set of Programs defined in installation data
     */
    private void complimentInstallationsDataWithInstalledProgramsWithSettings(
            SortedSet<Program> programsDefinedBySettings,
            SortedSet<Program> programsDefinedByInstallationsData) {
        for (Program installedProgram : programsDefinedByInstallationsData)
            if (programsDefinedBySettings.contains(installedProgram)) {
                installedProgram.setDevelopmentVersion(Iterables.find(programsDefinedBySettings,
                        Predicates.equalTo(installedProgram)).isDevelopmentVersion());
                this.installationsData.add(installedProgram);
            }
    }

    /**
     * Compliments installation data with Programs that are not installed
     * (according to passed parameters) but have defined settings (and as such
     * can be installed).
     * 
     * <p>
     * Second param's Programs should have all important information defined, as
     * they will be used for filling gaps.
     * </p>
     * 
     * @see #setInstallationsData(SortedSet)
     * @see #complimentInstallationsDataWithInstalledProgramsWithSettings(SortedSet,
     *      SortedSet)
     * 
     * @param programsDefinedBySettings
     * @param programsDefinedByInstallationsData
     */
    private void complimentInstallationsDataWithNotInstalledButDefinedBySettings(
            SortedSet<Program> programsDefinedBySettings,
            SortedSet<Program> programsDefinedByInstallationsData) {
        for (Program definedProgram : programsDefinedBySettings)
            if (!this.installationsData.contains(definedProgram))
                this.installationsData.add(definedProgram);
    }
}
