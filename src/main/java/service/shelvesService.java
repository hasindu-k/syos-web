package service;

import dao.ShelfDao;

public class shelvesService {

    private ShelfDao shelvesDao;

    public shelvesService() {
        this.shelvesDao = new ShelfDao();
    }

    public int updateShelves(int billID) {
        return shelvesDao.updateShelves(billID);
//        return 1;
    }

    public int updateSales(int billID) {
        return shelvesDao.updateSales(billID);
    }
    
    public int reshelf(){
        return shelvesDao.reshelf();
    }
}
