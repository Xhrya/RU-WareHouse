package warehouse;

/*
 *
 * This class implements a warehouse on a Hash Table like structure, 
 * where each entry of the table stores a priority queue. 
 * Due to your limited space, you are unable to simply rehash to get more space. 
 * However, you can use your priority queue structure to delete less popular items 
 * and keep the space constant.
 * 
 * @author Ishaan Ivaturi
 */ 
public class Warehouse {
    private Sector[] sectors;
    
    // Initializes every sector to an empty sector
    public Warehouse() {
        sectors = new Sector[10];

        for (int i = 0; i < 10; i++) {
            sectors[i] = new Sector();
        }
    }
    
    /**
     * Provided method, code the parts to add their behavior
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void addProduct(int id, String name, int stock, int day, int demand) {
        evictIfNeeded(id);
        addToEnd(id, name, stock, day, demand);
        fixHeap(id);
    }

    /**
     * Add a new product to the end of the correct sector
     * Requires proper use of the .add() method in the Sector class
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    private void addToEnd(int id, String name, int stock, int day, int demand) {
        // IMPLEMENT THIS METHOD
        
        Product p = new Product(id, name, stock, day, demand);
        int num = id % 10;
        sectors[num].add(p);
                   
    }

    /**
     * Fix the heap structure of the sector, assuming the item was already added
     * Requires proper use of the .swim() and .getSize() methods in the Sector class
     * @param id The id of the item which was added
     */
    private void fixHeap(int id) {
        // IMPLEMENT THIS METHOD
        int num = id%10;
        int size = sectors[num].getSize();
        sectors[num].swim(size);
        
    }

    /**
     * Delete the least popular item in the correct sector, only if its size is 5 while maintaining heap
     * Requires proper use of the .swap(), .deleteLast(), and .sink() methods in the Sector class
     * @param id The id of the item which is about to be added
     */
    private void evictIfNeeded(int id) {
       // IMPLEMENT THIS METHOD
       int num = id%10;
       int size = sectors[num].getSize();
       if(size == 5){
           sectors[num].swap(1, size);
           sectors[num].deleteLast();
           sectors[num].sink(1);
       }

    }

    /**
     * Update the stock of some item by some amount
     * Requires proper use of the .getSize() and .get() methods in the Sector class
     * Requires proper use of the .updateStock() method in the Product class
     * @param id The id of the item to restock
     * @param amount The amount by which to update the stock
     */
    public void restockProduct(int id, int amount) {
        // IMPLEMENT THIS METHOD
        int num = id % 10; 
        for(int i = 1; i<= sectors[num].getSize(); i++){
            Product p = sectors[num].get(i);
            if(p.getId() == id){
                p.updateStock(amount);
                return;
            }
        }
        return;
    }
    
    /**
     * Delete some arbitrary product while maintaining the heap structure in O(logn)
     * Requires proper use of the .getSize(), .get(), .swap(), .deleteLast(), .sink() and/or .swim() methods
     * Requires proper use of the .getId() method from the Product class
     * @param id The id of the product to delete
     */
    public void deleteProduct(int id) {
        // IMPLEMENT THIS METHOD
        int num = id % 10;
        for(int i = 1; i<= sectors[num].getSize(); i++){
            Product p = sectors[num].get(i);
            if(p.getId() == id){
                sectors[num].swap(i, sectors[num].getSize());
                sectors[num].deleteLast();
                sectors[num].sink(i);
                return;
            }
        }
        return;
    }
    
    /**
     * Simulate a purchase order for some product
     * Requires proper use of the getSize(), sink(), get() methods in the Sector class
     * Requires proper use of the getId(), getStock(), setLastPurchaseDay(), updateStock(), updateDemand() methods
     * @param id The id of the purchased product
     * @param day The current day
     * @param amount The amount purchased
     */
    public void purchaseProduct(int id, int day, int amount) {
        // IMPLEMENT THIS METHOD
        int num = id % 10;
        for(int i = 1; i<=sectors[num].getSize(); i++){
            Product p = sectors[num].get(i);
            if(p.getId() == id){
                if(p.getStock()>= amount){
                    p.setLastPurchaseDay(day);
                    p.updateStock(-amount);
                    p.updateDemand(amount);
                    sectors[num].sink(i);
                    return;
                }
                return;
            }
        }
    }
    
    /**
     * Construct a better scheme to add a product, where empty spaces are always filled
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void betterAddProduct(int id, String name, int stock, int day, int demand) {
        // IMPLEMENT THIS METHOD
        Product p = new Product(id, name, stock, day, demand);
        int num = id % 10;
        if(sectors[num].getSize()<5){
            addToEnd(id, name, stock, day, demand);
            fixHeap(id);
            return;
        }
        else{
            int i = (num+1) % 10;
            while(i<sectors.length){ 
                if(sectors[i].getSize()<5){
                    sectors[i].add(p);
                    int size = sectors[i].getSize();
                    sectors[i].swim(size);
                    //fixHeap(id);
                    return;
                }
                if(i==num){
                    evictIfNeeded(id);
                    sectors[i].add(p);
                    fixHeap(id);
                    return;
                }
                i = (i+1) % 10;
            }
        }
        
    }

    /*
     * Returns the string representation of the warehouse
     */
    public String toString() {
        String warehouseString = "[\n";

        for (int i = 0; i < 10; i++) {
            warehouseString += "\t" + sectors[i].toString() + "\n";
        }
        
        return warehouseString + "]";
    }

    /*
     * Do not remove this method, it is used by Autolab
     */ 
    public Sector[] getSectors () {
        return sectors;
    }
}
