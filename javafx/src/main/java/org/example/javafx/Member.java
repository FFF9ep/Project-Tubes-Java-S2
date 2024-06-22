package org.example.javafx;

public class Member {
    private String namaLengkap;
    private long nim;
    private String password;

    public Member(String namaLengkap, long nim, String password) {
        this.namaLengkap = namaLengkap;
        this.nim = nim;
        this.password = password;
    }

    // Getters and Setters
    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public long getNim() {
        return nim;
    }

    public void setNim(long nim) {
        this.nim = nim;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
