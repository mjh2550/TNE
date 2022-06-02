package com.solmi.biobrainexample.bio.data

import kotlinx.coroutines.flow.Flow

class BioRepository(private val bioDao : BioDao) {

    val allDatas : Flow<List<Bio>> = bioDao.getSelectBioList()

    suspend fun insert(bio: Bio){
        bioDao.insert(bio)
    }

    suspend fun deleteAll(){
        bioDao.deleteAll()
    }
}