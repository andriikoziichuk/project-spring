package com.eproject.library.alghorithms;

import com.eproject.library.model.Customer;

import java.util.Comparator;

public class CustomerComparator implements Comparator<Customer> {
    @Override
    public int compare(Customer o1, Customer o2) {
        return String.CASE_INSENSITIVE_ORDER.compare(o1.getUsername(), o2.getUsername());
    }
}
