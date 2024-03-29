package com.autoupdater.client.xml.creators;

import java.io.File;
import java.io.IOException;
import java.util.SortedSet;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.autoupdater.client.models.Package;
import com.autoupdater.client.models.Program;
import com.autoupdater.client.models.VersionNumber;
import com.autoupdater.client.xml.schema.InstallationDataSchema;
import com.google.common.io.Files;

/**
 * Creates installation data XML.
 * 
 * <p>
 * Document generated by this creator can be parsed by InstallationDataParser.
 * </p>
 * 
 * @see com.autoupdater.client.xml.creators.XMLCreationConfiguration
 * @see com.autoupdater.client.xml.parsers.InstallationDataParser
 * @see com.autoupdater.client.xml.schema.InstallationDataSchema
 */
public class InstallationDataXMLCreator {
    /**
     * Creates XML document with installation data and stores it info file.
     * 
     * @param destination
     *            destination file
     * @param installationData
     *            programs which data needs to be saved
     * @throws IOException
     *             thrown when error occurs during storing data to file
     */
    public void createXML(File destination, SortedSet<Program> installationData) throws IOException {
        Document installationDataXML = DocumentHelper.createDocument();
        installationDataXML.addComment(XMLCreationConfiguration.DO_NOT_EDIT_FILE_MANUALLY_WARNING);
        Element installed = installationDataXML.addElement(InstallationDataSchema.installed);
        addPrograms(installed, installationData);
        Files.write(installationDataXML.asXML(), destination, XMLCreationConfiguration.XML_ENCODING);
    }

    /**
     * Creates node for each programs.
     * 
     * @param installed
     *            root element of document
     * @param installationData
     *            programs which data needs to be saved
     */
    private void addPrograms(Element installed, SortedSet<Program> installationData) {
        if (installationData != null)
            for (Program program : installationData)
                if (!program.getPackages().isEmpty())
                    addProgram(installed, program);
    }

    /**
     * Crates node for single program.
     * 
     * @param installed
     *            root element of document
     * @param program
     *            program which data needs to be saved
     */
    private void addProgram(Element installed, Program program) {
        Element programNode = installed.addElement(InstallationDataSchema.Installed.program);
        programNode.addAttribute(InstallationDataSchema.Installed.Program.name, program.getName());
        programNode.addAttribute(InstallationDataSchema.Installed.Program.pathToDirectory,
                program.getPathToProgramDirectory());
        programNode.addAttribute(InstallationDataSchema.Installed.Program.serverAddress,
                program.getServerAddress());

        for (Package _package : program.getPackages())
            if (!_package.getVersionNumber().equals(VersionNumber.UNVERSIONED))
                addPackage(programNode, _package);
    }

    /**
     * Creates node for each package.
     * 
     * @param programNode
     *            program's node
     * @param _package
     *            packages which data needs to be saved
     */
    private void addPackage(Element programNode, Package _package) {
        Element packageNode = programNode
                .addElement(InstallationDataSchema.Installed.Program._package);
        packageNode.addAttribute(InstallationDataSchema.Installed.Program.Package.name,
                _package.getName());
        packageNode.addAttribute(InstallationDataSchema.Installed.Program.Package.version, _package
                .getVersionNumber().toString());
    }
}
