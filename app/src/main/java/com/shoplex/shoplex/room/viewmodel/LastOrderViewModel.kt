package com.shoplex.shoplex.room.viewmodel

/*
class LastOrderViewModel(application: Application) :AndroidViewModel(application) {

    private val readAllLastOrder : LiveData<List<Order>>
    private val lastOrderRepo : LastOrderRepo

    init {
        val lastOrderDoa = ShoplexDataBase.getDatabase(application).lastOrderDoa()
        lastOrderRepo = LastOrderRepo(lastOrderDoa)
        readAllLastOrder = lastOrderRepo.readAllLastOrder
    }

    fun addLastOrder(lastOrder: List<Order>){
        viewModelScope.launch(Dispatchers.IO) {
            lastOrderRepo.addLastOrder(lastOrder)
        }
    }
}*/