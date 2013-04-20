package com.autoupdater.client.models;

import java.util.Comparator;

import com.autoupdater.client.utils.comparables.Comparables;
import com.google.common.base.Objects;

/**
 * Class representing single change from Package's changelog.
 */
public class ChangelogEntry implements IModel<ChangelogEntry> {
    private String changes;
    private VersionNumber versionNumber;

    ChangelogEntry() {
    }

    /**
     * Returns change's description.
     * 
     * @return change's description
     */
    public String getChanges() {
        return changes;
    }

    /**
     * Sets change's description
     * 
     * @param changes
     *            change's description
     */
    void setChanges(String changes) {
        this.changes = changes != null ? changes : "";
    }

    /**
     * Returns version number in which change was done
     * 
     * @return version number in which change was done
     */
    public VersionNumber getVersionNumber() {
        return versionNumber;
    }

    /**
     * Sets version number in which change was done
     * 
     * @param versionNumber
     *            version number in which change was done
     */
    void setVersionNumber(String versionNumber) {
        this.versionNumber = new VersionNumber(versionNumber);
    }

    /**
     * Sets version number in which change was done
     * 
     * @param versionNumber
     *            version number in which change was done
     */
    void setVersionNumber(VersionNumber versionNumber) {
        this.versionNumber = versionNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ChangelogEntry))
            return false;
        else if (obj == this)
            return true;

        ChangelogEntry changelog = (ChangelogEntry) obj;
        return Objects.equal(changes, changelog.changes)
                && Objects.equal(versionNumber, changelog.versionNumber);
    }

    @Override
    public int hashCode() {
        return (int) Math.pow(changes.hashCode(), 10) + versionNumber.hashCode();
    }

    @Override
    public int compareTo(ChangelogEntry o) {
        if (o == null)
            return 1;
        else if (o == this)
            return 0;
        else if (!Objects.equal(versionNumber, o.versionNumber))
            return Comparables.compare(versionNumber, o.versionNumber);
        return Comparables.compare(changes, o.changes);
    }

    @Override
    public String toString() {
        return versionNumber + ":\n" + changes;
    }

    @Override
    public Comparator<ChangelogEntry> getInstallationsServerPropertiesComparator() {
        return new GeneralComparator();
    }

    @Override
    public Comparator<ChangelogEntry> getLocalInstallationsComparator() {
        return new GeneralComparator();
    }

    @Override
    public Comparator<ChangelogEntry> getLocal2ServerComparator() {
        return new GeneralComparator();
    }

    private class GeneralComparator implements Comparator<ChangelogEntry> {
        @Override
        public int compare(ChangelogEntry o1, ChangelogEntry o2) {
            if (o1 == null)
                return o2 == null ? 0 : -1;
            if (Objects.equal(o1.versionNumber, o2.versionNumber))
                Comparables.compare(o1.versionNumber, o2.versionNumber);
            return Comparables.compare(o1.changes, o2.changes);
        }
    }
}
