package com.openclassrooms.realestatemanager.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.data.database.converters.PointOfInterestConverter
import com.openclassrooms.realestatemanager.data.database.dao.EstateDao
import com.openclassrooms.realestatemanager.data.database.dao.PhotoDao
import com.openclassrooms.realestatemanager.data.database.dao.RealEstateAgentDao
import com.openclassrooms.realestatemanager.data.database.entities.EstateEntity
import com.openclassrooms.realestatemanager.data.database.entities.PhotoEntity
import com.openclassrooms.realestatemanager.data.enums.PropertyType
import com.openclassrooms.realestatemanager.data.database.entities.RealEstateAgentEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [EstateEntity::class, PhotoEntity::class, RealEstateAgentEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(PointOfInterestConverter::class)
abstract class RealEstateManagerDatabase : RoomDatabase() {

    // --- DAO ---
    abstract fun estateDao(): EstateDao
    abstract fun photoDao(): PhotoDao
    abstract fun realEstateAgentDao(): RealEstateAgentDao

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    database.withTransaction {
                        prepopulateDatabase(
                            database.estateDao(),
                            database.photoDao(),
                            database.realEstateAgentDao()
                        )
                    }
                }
            }
        }

        suspend fun prepopulateDatabase(
            estateDao: EstateDao,
            photoDao: PhotoDao,
            realEstateAgentDao: RealEstateAgentDao
        ) {
            val agent1 =
                realEstateAgentDao.insertRealEstateAgent(RealEstateAgentEntity(displayName = "Vincent Jessé"))
            val agent2 =
                realEstateAgentDao.insertRealEstateAgent(RealEstateAgentEntity(displayName = "Olivier Jourdain"))
            val agent3 =
                realEstateAgentDao.insertRealEstateAgent(RealEstateAgentEntity(displayName = "Yolande Antoinette"))

            val estate1 = estateDao.insertEstate(
                EstateEntity(
                    type = PropertyType.HOUSE,
                    price = 825000,
                    propertyArea = 2323,
                    numberOfBathrooms = 3,
                    numberOfBedrooms = 3,
                    description = "Stunning and incredibly well-preserved home in the heart of Sloan’s Lake and the Witter-Cofield Historic District. This turn-of-the-century, light-filled bungalow offers three beds, two and a half baths, and over 2,000 square feet of comfortable living space. Updated kitchen on main floor with stainless steel appliances, granite countertops, and ample cabinet space. Mint condition exposed brick and pristine Douglas Fir hardwood flooring highlight original built-ins including mantle and hutch of an impeccable quality in this time period home. Main floor includes two bedrooms, with basement bed option to be utilized as a Primary Suite complete with luxurious spa-like five-piece bath offering soaker tub, double shower heads and a tasteful accent tile that adds pop to the space. Basement also showcases the perfect TV den pre-wired for surround sound, wet bar, additional flex room for home office, gym, storage, and laundry. Backyard is ideally suited for entertaining with fire pit, awning with ceiling fan, concrete pad for a hot tub, and ready for your garden projects. Covered front porch privacy wall created as the home is framed by mature pine trees. Oversized detached 2-car garage with bonus storage space/work bench and additional parking spots adjacent to rear fence. A quick bike ride or walk to Sloan’s Lake, West Colfax, LoHi, or Highland Square for access to some of the best restaurants, bars, and coffee shops that NW Denver has to offer!",
                    address = "2425 Federal Boulevard",
                    additionalAddressLine = null,
                    city = "Denver",
                    zipCode = "80211",
                    country = "United State",
                    latitude = 39.753348594578085,
                    longitude = -105.02557896690276,
                    pointsOfInterest = null,
                    available = true,
                    entryDate = System.currentTimeMillis(),
                    saleDate = null,
                    realEstateAgentId = agent1
                )
            )

            val estate2 = estateDao.insertEstate(
                EstateEntity(
                    type = PropertyType.DUPLEX,
                    price = 795000,
                    propertyArea = 2324,
                    numberOfBathrooms = 4,
                    numberOfBedrooms = 3,
                    description = "You will love this new construction duplex from renowned builder Gallup Development. Within close proximity to Sloan’s Lake, downtown Denver, and walking distance to light rail this Villa Park address is centrally located with access to the best of Denver. Modern-farmhouse architecture showcases a highly functional floor plan. Open main level with proper dining room, high-end kitchen with 16′ Caesarstone center island, great room accented by a stacked stone mantel and modern gas fireplace, mudroom with built-in bench/coat hooks, and powder bath. Double glass doors open to landscaped backyard and detached 2-car garage. Second floor features the coveted 3 bedroom layout with laundry. Primary bedroom captures western exposure with generous 12′ vaulted ceilings and offers ensuite 5-piece bath with jetted-tub. Two secondary bedrooms on same level are both large enough to accommodate queen sized beds, fully carpeted and offer oversized closets. Third floor includes another modern 3/4 bath and Rec Room suited for movie night, game watch, or home office setup and steps out to an oversized east-facing deck, perfect for morning coffee. At \$374/ft a tremendous value for a brand new townhouse!",
                    address = "1036 Knox Ct",
                    additionalAddressLine = null,
                    city = "Denver",
                    zipCode = "80204",
                    country = "United State",
                    latitude = 39.733922539377986,
                    longitude = -105.03180154232744,
                    pointsOfInterest = null,
                    available = true,
                    entryDate = System.currentTimeMillis(),
                    saleDate = null,
                    realEstateAgentId = agent2
                )
            )
            val estate3 = estateDao.insertEstate(
                EstateEntity(
                    type = PropertyType.LOFT,
                    price = 795000,
                    propertyArea = 2324,
                    numberOfBathrooms = 2,
                    numberOfBedrooms = 2,
                    description = "Located in the popular, well maintained and conveniently located Hunters Hill Community. Ready to move in, welcome to this beautifully maintained 2 bed/2 bath unit. Featuring many updated mechanicals including newer furnace, newer water heater, newer electrical panel. Other updates include new carpet, new paint, new baseboards in bedrooms, new primary shower, many new light fixtures, newer patio door, new vent covers, and lots more. This home has a great covered patio that backs onto green space, tennis courts and pool. Conveniently located and just a 5 min walk to local restaurants, coffee shop, Walgreen’s, hotel, and many other small businesses. A short 5-minute drive to Dry Creek Light Rail Station (18 min walk) or a 5 minute drive to Park Meadows Shopping with many restaurant and entertainment options. 2 miles to Fiddler's Green DTC and more. Easy access to I-25 and 470. Don’t miss out on this opportunity to own a very affordable unit in a great location!",
                    address = "7307 S Xenia",
                    additionalAddressLine = "Cir B",
                    city = "Centennial",
                    zipCode = "80112",
                    country = "United State",
                    latitude = 39.58369307623072,
                    longitude = -104.88624551681441,
                    pointsOfInterest = null,
                    available = true,
                    entryDate = System.currentTimeMillis(),
                    saleDate = null,
                    realEstateAgentId = agent1
                )
            )

            photoDao.insertPhotos(
                listOf(
                    PhotoEntity(
                        uri = "https://source.unsplash.com/random/?House&1",
                        description = "Building Entrance",
                        estateId = estate1
                    ),
                    PhotoEntity(
                        uri = "https://source.unsplash.com/random/?House&2",
                        description = "Building Exterior",
                        estateId = estate1
                    ),
                    PhotoEntity(
                        uri = "https://source.unsplash.com/random/?House&3",
                        description = "Living Room",
                        estateId = estate1
                    ),
                    PhotoEntity(
                        uri = "https://source.unsplash.com/random/?House&4",
                        description = "Dining Room",
                        estateId = estate1
                    ),
                    PhotoEntity(
                        uri = "https://source.unsplash.com/random/?House&5",
                        description = "Kitchen",
                        estateId = estate1
                    ),
                    PhotoEntity(
                        uri = "https://source.unsplash.com/random/?House&6",
                        description = "Dining Room",
                        estateId = estate2
                    ),
                    PhotoEntity(
                        uri = "https://source.unsplash.com/random/?House&7",
                        description = "Living Room",
                        estateId = estate2
                    ),
                    PhotoEntity(
                        uri = "https://source.unsplash.com/random/?House&8",
                        description = "Bedroom",
                        estateId = estate2
                    ),
                    PhotoEntity(
                        uri = "https://source.unsplash.com/random/?House&9",
                        description = "Building Entrance",
                        estateId = estate3
                    ),
                    PhotoEntity(
                        uri = "https://source.unsplash.com/random/?House&10",
                        description = "Living Room",
                        estateId = estate3
                    ),
                    PhotoEntity(
                        uri = "https://source.unsplash.com/random/?House&11",
                        description = "Bedroom",
                        estateId = estate3
                    ),
                    PhotoEntity(
                        uri = "https://source.unsplash.com/random/?House&12",
                        description = "Kitchen",
                        estateId = estate3
                    )
                )
            )
        }
    }

    companion object {
        // --- SINGLETON ---
        @Volatile
        private var INSTANCE: RealEstateManagerDatabase? = null
        private var DATABASE_FILE_NAME: String = "real_estate_manager_database.db"

        // --- INSTANCE ---
        fun getInstance(context: Context, scope: CoroutineScope): RealEstateManagerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    RealEstateManagerDatabase::class.java, DATABASE_FILE_NAME
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

