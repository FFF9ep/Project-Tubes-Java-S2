const bcrypt = require("bcrypt")

async function generateHash(pass) {
    const saltRounds = 10
    try {
        const hash = await bcrypt.hash(pass, saltRounds);
        // console.log('Hashed password:', hash);
        return hash;
    } catch (err) {
        console.error(err);
    }
}

module.exports = generateHash