const API = "http://localhost:8080/api";

// ─── Load everything when page opens ───
window.onload = () => {
    loadPersons();
    loadExpenses();
};

// ─── PERSON FUNCTIONS ───

async function loadPersons() {
    const res = await fetch(`${API}/persons`);
    const persons = await res.json();

    // Update person list
    const list = document.getElementById("personList");
    list.innerHTML = "";
    persons.forEach(p => {
        list.innerHTML += `
            <li>
                👤 ${p.name} ${p.email ? '(' + p.email + ')' : ''}
                <button onclick="deletePerson(${p.id})">Delete</button>
            </li>`;
    });

    // Update dropdown in expense form
    const dropdown = document.getElementById("paidBy");
    dropdown.innerHTML = `<option value="">-- Who Paid? --</option>`;
    persons.forEach(p => {
        dropdown.innerHTML += `<option value="${p.id}">${p.name}</option>`;
    });
}

async function addPerson() {
    const name = document.getElementById("personName").value.trim();
    const email = document.getElementById("personEmail").value.trim();

    if (!name) {
        alert("Please enter a name!");
        return;
    }

    await fetch(`${API}/persons`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, email })
    });

    document.getElementById("personName").value = "";
    document.getElementById("personEmail").value = "";
    loadPersons();
}

async function deletePerson(id) {
    await fetch(`${API}/persons/${id}`, { method: "DELETE" });
    loadPersons();
    loadExpenses();
}

// ─── EXPENSE FUNCTIONS ───

async function loadExpenses() {
    const res = await fetch(`${API}/expenses`);
    const expenses = await res.json();

    const list = document.getElementById("expenseList");
    list.innerHTML = "";

    if (expenses.length === 0) {
        list.innerHTML = "<li>No expenses yet!</li>";
        return;
    }

    expenses.forEach(e => {
        list.innerHTML += `
            <li>
                🧾 ${e.description} — ₹${e.amount} (Paid by ${e.paidBy.name})
                <button onclick="deleteExpense(${e.id})">Delete</button>
            </li>`;
    });
}

async function addExpense() {
    const description = document.getElementById("expenseDesc").value.trim();
    const amount = document.getElementById("expenseAmount").value;
    const paidById = document.getElementById("paidBy").value;

    if (!description || !amount || !paidById) {
        alert("Please fill all fields!");
        return;
    }

    await fetch(`${API}/expenses`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ description, amount: parseFloat(amount), paidById: parseInt(paidById) })
    });

    document.getElementById("expenseDesc").value = "";
    document.getElementById("expenseAmount").value = "";
    document.getElementById("paidBy").value = "";
    loadExpenses();
}

async function deleteExpense(id) {
    await fetch(`${API}/expenses/${id}`, { method: "DELETE" });
    loadExpenses();
}

// ─── SPLIT CALCULATION ───

async function calculateSplit() {
    const res = await fetch(`${API}/expenses/split`);
    const data = await res.json();

    const resultDiv = document.getElementById("splitResult");
    resultDiv.innerHTML = "";

    if (Object.keys(data).length === 0) {
        resultDiv.innerHTML = "<p>No data to calculate!</p>";
        return;
    }

    for (const [name, balance] of Object.entries(data)) {
        let cssClass = balance > 0 ? "positive" : balance < 0 ? "negative" : "neutral";
        let message = balance > 0
            ? `${name} should receive ₹${Math.abs(balance)}`
            : balance < 0
            ? `${name} owes ₹${Math.abs(balance)}`
            : `${name} is settled up ✅`;

        resultDiv.innerHTML += `<div class="${cssClass}">${message}</div>`;
    }
}