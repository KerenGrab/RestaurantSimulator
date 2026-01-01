package com.keren.restaurantsimulator.model;
import com.keren.restaurantsimulator.exception.TableNotFoundException;
import java.util.*;

public class Restaurant {
    private final Menu menu;
    private final Map<Integer, Table> tablesByNumber;

    public Restaurant(Menu menu) {
        if (menu == null) {
            throw new IllegalArgumentException("menu cannot be null");
        }
        this.menu = menu;
        this.tablesByNumber = new LinkedHashMap<>();
    }

    public Menu getMenu(){
        return menu;
    }

    public Table getTableById(int id) {
        Table table = tablesByNumber.get(id);
        if (table == null) {
            throw new TableNotFoundException("Table with id " + id + " not found");
        }
        return table;
    }

    public void addTable(Table table) {
        if (table == null) {
            throw new IllegalArgumentException("table cannot be null");
        }

        int number = table.getTableNumber();
        if (tablesByNumber.containsKey(number)) {
            throw new IllegalArgumentException("Table with number " + number + " already exists");
        }

        tablesByNumber.put(number, table);
    }
    public boolean removeTableById(int id){
        return tablesByNumber.remove(id) != null;
    }

    public Collection<Table> listTables() {
        return Collections.unmodifiableCollection(tablesByNumber.values());
    }

    public boolean containsTable(int id) {
        return tablesByNumber.containsKey(id);
    }

    public int size() {
        return tablesByNumber.size();
    }

}
