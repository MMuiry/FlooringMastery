package com.mthree.floorMastery.dao;

import com.mthree.floorMastery.exceptions.PersistenceException;

public interface FlooringMasterExportDao {

    void export() throws PersistenceException;

}
