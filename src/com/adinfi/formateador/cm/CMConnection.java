package com.adinfi.formateador.cm;

import com.adinfi.formateador.util.ApplicationProperties;
import com.adinfi.formateador.util.Utilerias;
import com.ibm.mm.sdk.common.DKException;
import com.ibm.mm.sdk.server.DKDatastoreICM;

public class CMConnection {

    private DKDatastoreICM dsICM = null;

    public synchronized DKDatastoreICM getDataStoreCM() {
        return dsICM;
    }

    public synchronized boolean connectCM() {
        if (dsICM != null && dsICM.isConnected() == true) {
            return true;
        }

        String dbConnectionLS = Utilerias.getProperty(ApplicationProperties.ICM_LIBRARY);
        String dbConnectionUser = Utilerias.getProperty(ApplicationProperties.ICM_ADMIN);
        String dbConnectionPassword = Utilerias.getProperty(ApplicationProperties.ICM_PASSWORD);
        String dbConnectionSchema = Utilerias.getProperty(ApplicationProperties.ICM_SCHEMA);

        boolean connectionActive = true;
        try {
            dsICM = new DKDatastoreICM();
            dsICM.connect(dbConnectionLS, dbConnectionUser, dbConnectionPassword, dbConnectionSchema);
        } catch (DKException e) {
            printException(e);
            connectionActive = false;
        } catch (Exception e) {
            printException(e);
            connectionActive = false;
        }
        return connectionActive;
    }

    public synchronized void disconectCM() {
        try {
            if (dsICM != null && dsICM.isConnected()) {
                dsICM.disconnect();
                dsICM.destroy();
                //  Util.logger(getClass()).error("Disconnected from DataStore");
            }
        } catch (DKException e) {
            printException(e);
        } catch (Exception e) {
            printException(e);
        }
    }

    public synchronized void printException(DKException e) {
        Utilerias.logger(getClass()).error(" Name: " + e.name());
        Utilerias.logger(getClass()).error(" Message: " + e.getMessage());
        Utilerias.logger(getClass()).error(" Message ID: " + e.getErrorId());
        Utilerias.logger(getClass()).error(" Error State: " + e.errorState());
        Utilerias.logger(getClass()).error(" Error Code: " + e.errorCode());
        Utilerias.logger(getClass()).error(e);
    }

    public synchronized void printException(Exception e) {
        Utilerias.logger(getClass()).error(" Name: " + e.getClass().getName());
        Utilerias.logger(getClass()).error(" Message: " + e.getMessage());
    }
}
