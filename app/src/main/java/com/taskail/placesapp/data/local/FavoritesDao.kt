package com.taskail.placesapp.data.local

import android.arch.persistence.room.*
import com.taskail.placesapp.data.models.FavoritePlace
import io.reactivex.Flowable

/**
 *Created by ed on 4/13/18.
 */

@Dao
interface FavoritesDao {

    /**
     * get all favorites from the favorites database table
     * @return a list of locations
     */
    @Query("SELECT * FROM Favorites") fun getLocations(): Flowable<List<FavoritePlace>>

    /**
     * insert a new favorite into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)fun addLocation(location: FavoritePlace)

    /**
     * remove a favorite from the database
     */
    @Delete fun removeFavorite(favoritePlace: FavoritePlace)

}