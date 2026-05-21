package com.example.billsplit.controller;

import com.example.billsplit.model.Expense;
import com.example.billsplit.model.Person;
import com.example.billsplit.service.ExpenseService;
import com.example.billsplit.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private PersonService personService;

    // GET all expenses
    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    // POST add an expense
    @PostMapping
    public Expense addExpense(@RequestBody ExpenseRequest request) {
        Person paidBy = personService.getAllPersons()
                .stream()
                .filter(p -> p.getId() == request.getPaidById())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Person not found"));

        Expense expense = new Expense();
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setPaidBy(paidBy);

        return expenseService.addExpense(expense);
    }

    // DELETE an expense
    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable int id) {
        expenseService.deleteExpense(id);
    }

    // GET split calculation
    @GetMapping("/split")
    public Map<String, Double> getSplit() {
        return expenseService.calculateSplit();
    }

    // Inner class for request body
    static class ExpenseRequest {
        private String description;
        private double amount;
        private int paidById;

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }

        public int getPaidById() { return paidById; }
        public void setPaidById(int paidById) { this.paidById = paidById; }
    }
}

