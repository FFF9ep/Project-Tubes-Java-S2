const express = require("express")
const mysql = require("mysql2")
const cors = require("cors")
const bcrypt = require("bcrypt")
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

        const query = 'SELECT password FROM members WHERE nim = ?';

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
                return res.status(200).send("Password is correct");
            } else {
                return res.status(401).send("Password is incorrect");
            }
        });

    } catch (err) {
        console.error(err);
        return res.status(500).send("Internal server error");
    }
});

app.post('/api/add-book', (req, res) => {
    let { codeBook, author, publisher, datePublished, about, pages, imgBook } = req.body;

    if (!about || about.trim() === "") {
        about = 'Belum ditambahkan';
    }
    if (!pages || pages.trim() === "") {
        pages = 'Belum ditambahkan';
    }

    const query = `
        INSERT INTO books (codeBook, author, publisher, datePublished, about, pages, imgBook) 
        VALUES (?, ?, ?, ?, ?, ?, ?)
    `;

    db.query(query, [codeBook, author, publisher, datePublished, about, pages, imgBook], (err, result) => {
        if (err) {
            console.error('Error inserting book:', err);
            res.status(500).json({ error: 'Error inserting book' });
            return;
        }
        res.status(201).json({ message: 'Book added successfully', bookId: result.insertId });
    });
});

app.listen(PORT, () => {
    console.log(`Server telah berjalan di port ${PORT}`)
})
