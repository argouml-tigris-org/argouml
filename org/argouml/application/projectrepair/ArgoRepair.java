package org.argouml.application.projectrepair;

/**
 * Repairs a .argo file.
 * 
 * @author jaap.branderhorst@xs4all.nl
 */
public class ArgoRepair extends AbstractRepairImpl {

    /**
     * Constructor for ArgoRepair.
     */
    public ArgoRepair() {
        super();
    }

    /**
     * There are no known defects at the moment with .argo files. Therefore
     * this method has an empty implementation.
     * @see org.argouml.filerepair.AbstractRepairImpl#internalRepairDocument()
     */
    protected void internalRepairDocument() {
    }

}
