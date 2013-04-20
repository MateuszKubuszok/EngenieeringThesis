package com.autoupdater.client.models;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import com.autoupdater.client.environment.settings.ProgramSettings;
import com.autoupdater.client.environment.settings.ProgramSettingsBuilder;
import com.autoupdater.client.models.Models.EComparisionType;
import com.autoupdater.client.utils.comparables.Comparables;
import com.google.common.base.Objects;

/**
 * Class representing Program - either on server or installed one.
 */
public class Program implements IModel<Program> {
    private String name;
    private String pathToProgramDirectory;
    private String serverAddress;

    private final SortedSet<Package> packages;
    private final SortedSet<BugEntry> bugs;
    private boolean developmentVersion;

    Program() {
        packages = new TreeSet<Package>();
        bugs = new TreeSet<BugEntry>();
    }

    public ProgramSettings findProgramSettings(SortedSet<ProgramSettings> programsSettings) {
        return Models.findEqual(
                programsSettings,
                ProgramSettingsBuilder.builder().setProgramName(name)
                        .setPathToProgramDirectory(pathToProgramDirectory)
                        .setServerAddress(serverAddress).build(),
                EComparisionType.LOCAL_INSTALLATIONS);
    }

    /**
     * Return's Program's name.
     * 
     * @return Program's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets Program's name.
     * 
     * @param name
     *            Program's name
     */
    void setName(String name) {
        this.name = name != null ? name : "";
    }

    public String getPathToProgramDirectory() {
        return pathToProgramDirectory;
    }

    void setPathToProgramDirectory(String pathToProgramDirectory) {
        this.pathToProgramDirectory = pathToProgramDirectory != null ? pathToProgramDirectory : "";
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress != null ? serverAddress : "";

        if (this.serverAddress.endsWith("/"))
            this.serverAddress = serverAddress.substring(0, this.serverAddress.length() - 1);
        if (!this.serverAddress.startsWith("http://") && !this.serverAddress.startsWith("https://"))
            this.serverAddress = "http://" + serverAddress;
    }

    /**
     * Whether Program is for development version.
     * 
     * @return whether Program is for development version
     */
    public boolean isDevelopmentVersion() {
        return developmentVersion;
    }

    /**
     * Sets whether Program is for development version
     * 
     * @param developmentVersion
     *            whether Program is for development version
     */
    public void setDevelopmentVersion(boolean developmentVersion) {
        this.developmentVersion = developmentVersion;
    }

    /**
     * Sets whether Program is for development version
     * 
     * @param developmentVersion
     *            whether Program is for development version
     */
    public void setDevelopmentVersion(String developmentVersion) {
        this.developmentVersion = "true".equalsIgnoreCase(developmentVersion);
    }

    /**
     * Returns Program's Packages.
     * 
     * @return Program's Packages
     */
    public SortedSet<Package> getPackages() {
        return packages;
    }

    /**
     * Sets Program's Packages
     * 
     * @param packages
     *            Program's Packages
     */
    public void setPackages(SortedSet<Package> packages) {
        this.packages.clear();
        if (packages != null) {
            this.packages.addAll(packages);
            configurePackages();
        }
    }

    /**
     * Returns Program's Bugs.
     * 
     * @return Program's Bugs
     */
    public SortedSet<BugEntry> getBugs() {
        return bugs;
    }

    /**
     * Sets Program's Bugs.
     * 
     * @param bugs
     *            Program's Bugs
     */
    public void setBugs(SortedSet<BugEntry> bugs) {
        this.bugs.clear();
        if (bugs != null)
            this.bugs.addAll(bugs);
    }

    /**
     * Whether such Package belongs to Program.
     * 
     * @param _package
     *            Package
     * @return true if such Package belongs to Program
     */
    public boolean hasMember(Package _package) {
        return packages != null && packages.contains(_package);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Program))
            return false;
        else if (obj == this)
            return true;

        Program program = (Program) obj;
        return Objects.equal(name, program.name)
                && Objects.equal(serverAddress, program.serverAddress)
                && Objects.equal(pathToProgramDirectory, program.pathToProgramDirectory);
    }

    @Override
    public int hashCode() {
        return ((int) Math.pow(name.hashCode(), 8)) + ((int) Math.pow(serverAddress.hashCode(), 4))
                + pathToProgramDirectory.hashCode();
    }

    @Override
    public int compareTo(Program o) {
        if (o == null)
            return 1;
        else if (o == this)
            return 0;
        else if (!Objects.equal(name, o.name))
            return Comparables.compare(name, o.name);
        else if (!Objects.equal(serverAddress, o.serverAddress))
            return Comparables.compare(serverAddress, o.serverAddress);
        return Comparables.compare(pathToProgramDirectory, o.pathToProgramDirectory);
    }

    private void configurePackages() {
        if (packages != null)
            for (Package _package : packages)
                _package.setProgram(this);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Comparator<Program> getInstallationsServerPropertiesComparator() {
        return new InstallationsServerPropertiesComparator();
    }

    @Override
    public Comparator<Program> getLocalInstallationsComparator() {
        return new LocalInstallationsComparator();
    }

    @Override
    public Comparator<Program> getLocal2ServerComparator() {
        return new Local2ServerComparator();
    }

    private class InstallationsServerPropertiesComparator implements Comparator<Program> {
        @Override
        public int compare(Program o1, Program o2) {
            if (o1 == null)
                return (o2 == null ? 0 : -1);
            else if (!Objects.equal(o1.name, o2.name))
                return Comparables.compare(o1.name, o2.name);
            else if (!Objects.equal(o1.serverAddress, o2.serverAddress))
                return Comparables.compare(o1.serverAddress, o2.serverAddress);
            return Comparables.compare(o1.pathToProgramDirectory, o2.pathToProgramDirectory);
        }
    }

    private class LocalInstallationsComparator implements Comparator<Program> {
        @Override
        public int compare(Program o1, Program o2) {
            if (o1 == null)
                return (o2 == null ? 0 : -1);
            else if (!Objects.equal(o1.name, o2.name))
                return Comparables.compare(o1.name, o2.name);
            else if (!Objects.equal(o1.serverAddress, o2.serverAddress))
                return Comparables.compare(o1.serverAddress, o2.serverAddress);
            return Comparables.compare(o1.pathToProgramDirectory, o2.pathToProgramDirectory);
        }
    }

    private class Local2ServerComparator implements Comparator<Program> {
        @Override
        public int compare(Program o1, Program o2) {
            return (o1 == null) ? (o2 == null ? 0 : -1) : Comparables.compare(o1.name, o2.name);
        }
    }
}
