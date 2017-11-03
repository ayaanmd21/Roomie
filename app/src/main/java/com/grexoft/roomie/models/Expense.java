package com.grexoft.roomie.models;

import java.util.List;



public class Expense {
    private final String title;
    private final Double amount;
    private final String description;
    private final long payingId;
    private final List<User> members;
    private final long id;

    public Expense(ExpenseBuilder expenseBuilder) {
        this.title = expenseBuilder.title;
        this.amount = expenseBuilder.amount;
        this.description = expenseBuilder.description;
        this.payingId = expenseBuilder.payingId;
        this.members = expenseBuilder.members;
        this.id=expenseBuilder.id;
    }


    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public long getPayingId() {
        return payingId;
    }

    public List<User> getMembers() {
        return members;
    }

    public static class ExpenseBuilder {
        private final String title;
        private Double amount;
        private String description;
        private long payingId;
        private List<User> members;
        private long id;


        public ExpenseBuilder(String title) {
            this.title = title;
        }

        public  ExpenseBuilder id(long id){
            this.id=id;
            return this;
        }
        public ExpenseBuilder amount(Double amount) {
            this.amount = amount;
            return this;
        }

        public ExpenseBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ExpenseBuilder payingId(long payingId) {
            this.payingId = payingId;
            return this;
        }

        public ExpenseBuilder members(List<User> members) {
            this.members = members;
            return this;
        }

        public Expense build() {
            return new Expense(this);
        }
    }
}
