const express = require("express")
const mysql = require("mysql2")
const cors = require("cors")
const bcrypt = require("bcrypt")
const moment = require("moment")
require('dotenv').config()

const generateHash = require("./utils/hashPass")

const app = express()

app.use(cors())
app.use(express.urlencoded({ extended: true }))
app.use(express.json())

const PORT = 5000

const db = mysql.createConnection({
    host: process.env.MYSQL_HOST,
    user: process.env.MYSQL_USER,
    password: process.env.MYSQL_PASS,
    database: process.env.MYSQL_DB
})

db.connect((err) => {
    console.log("Connecting to database...")
    if (err) {
        console.error("Error connecting to database:", err)
    } else {
        console.log("Connected to database")
    }
})

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< AUTH ENDPOINTs >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

app.post('/api/reg-member', async (req, res) => {
    const { namaLengkap, nim, password } = req.body;
    const encPass = await generateHash(password);
    const query = `INSERT INTO members (namaLengkap, nim, password) VALUES (?, ?, ?)`;
    console.log(req.body);

    try {
        db.query(query, [namaLengkap, nim, encPass], (err, result) => {
            if (err) {
                if (err.code === "ER_DUP_ENTRY") {
                    console.error("Error: Duplicate entry");
                    res.status(400).send("Duplicate entry");
                } else {
                    console.error("Error: ", err);
                    res.status(500).send("Error querying database");
                }
            } else {
                console.log(result);
                res.send("Data successfully inserted");
            }
        });
    } catch (error) {
        res.status(500).send("An error occurred");
    }
});


app.post('/api/login', async (req, res) => {
    try {
        const nim = req.body.nim;
        const plainPassword = req.body.password;

        if (!nim || !plainPassword) {
            return res.status(400).send("NIM and password are required");
        }

        const query = 'SELECT password, level FROM members WHERE nim = ?';

        db.query(query, [nim], async (err, results) => {
            if (err) {
                return res.status(500).send("Error while retrieving data");
            }

            if (results.length === 0) {
                return res.status(404).send("User not found");
            }

            const hashedPassword = results[0].password;

            const match = await bcrypt.compare(plainPassword, hashedPassword);

            if (match) {
                return res.status(200).json({ message: "Password is correct", level: results[0].level });
            } else {
                return res.status(401).send("Password is incorrect");
            }
        });

    } catch (err) {
        console.error(err);
        return res.status(500).send("Internal server error");
    }
});


// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< USERS ENDPOINTs >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


app.get('/api/get-all-members', (req, res) => {
    const query = 'SELECT * FROM members';

    db.query(query, (err, results) => {
        if (err) {
            console.error('Error retrieving members:', err);
            res.status(500).json({ error: 'Error retrieving members' });
            return;
        }
        res.status(200).json(results);
    });
});

app.put('/api/edit-member/:id', async (req, res) => {
    const { id } = req.params;
    const { email, password, imgMem } = req.body;

    let query = 'UPDATE members SET ';
    let fields = [];
    let values = [];

    if (email) {
        fields.push('email = ?');
        values.push(email);
    }
    if (password) {
        const hashedPassword = await generateHash(password);
        fields.push('password = ?');
        values.push(hashedPassword);
    }
    if (imgMem) {
        fields.push('imgMem = ?');
        values.push(imgMem);
    }

    if (fields.length === 0) {
        res.status(400).json({ error: 'No fields to update' });
        return;
    }

    query += fields.join(', ') + ' WHERE id = ?';
    values.push(id);

    db.query(query, values, (err, result) => {
        if (err) {
            console.error('Error updating member:', err);
            res.status(500).json({ error: 'Error updating member' });
            return;
        }
        res.status(200).json({ message: 'Member updated successfully' });
    });
});


// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< BOOKS ENDPOINTs >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

app.post('/api/add-book', (req, res) => {
    let { codeBook, title, author, publisher, datePublished, about, pages, imgBook } = req.body;

    if (!about || about.trim() === "") {
        about = 'Belum ditambahkan';
    }
    if (!pages || pages.trim() === "") {
        pages = 'Belum ditambahkan';
    }

    const query = `
        INSERT INTO books (codeBook, title, author, publisher, datePublished, about, pages, imgBook) 
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    `;

    db.query(query, [codeBook, title, author, publisher, datePublished, about, pages, imgBook], (err, result) => {
        if (err) {
            console.error('Error inserting book:', err);
            res.status(500).json({ error: 'Error inserting book' });
            return;
        }
        res.status(201).json({ message: 'Book added successfully', bookId: result.insertId });
    });
});

app.get('/api/get-all-books', (req, res) => {
    const query = 'SELECT * FROM books';

    db.query(query, (err, results) => {
        if (err) {
            console.error('Error retrieving books:', err);
            res.status(500).json({ error: 'Error retrieving books' });
            return;
        }
        res.status(200).json(results);
    });
});


// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< BORROW ENDPOINTs >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

app.post('/api/borrow', (req, res) => {
    let { idBook, idMem, dateBorrowed, dateBack } = req.body;

    if (!idBook || !idMem || !dateBorrowed || !dateBack) {
        return res.status(400).json({ message: 'All fields are required' });
    }

    dateBorrowed = moment(dateBorrowed, 'DD-MM-YYYY').format('YYYY-MM-DD');
    dateBack = moment(dateBack, 'DD-MM-YYYY').format('YYYY-MM-DD');


    const query = `INSERT INTO borrows (idBook, idMem, dateBorrowed, dateBack, extendedCount, isFinished) 
                   VALUES (?, ?, ?, ?, 0, 0)`;

    db.query(query, [idBook, idMem, dateBorrowed, dateBack], (err, result) => {
        if (err) {
            return res.status(500).json({ message: 'Database error', error: err });
        }

        res.status(201).json({ message: 'Book borrowed successfully', borrowId: result.insertId });
    });
});

app.get('/api/borrowed-books/:idMem', (req, res) => {
    const { idMem } = req.params;

    const query = `
      SELECT b.title, b.publisher, br.dateBorrowed, br.dateBack, br.extendedCount, br.isFinished
      FROM borrows br
      JOIN books b ON br.idBook = b.Id
      WHERE br.idMem = ?
    `;

    db.query(query, [idMem], (err, results) => {
        if (err) {
            return res.status(500).json({ message: 'Database error', error: err });
        }

        res.status(200).json(results);
    });
});


app.listen(PORT, () => {
    console.log(`Server telah berjalan di port ${PORT}`)
})
