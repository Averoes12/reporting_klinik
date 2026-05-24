CREATE DATABASE IF NOT EXISTS klinik;
USE klinik;

CREATE TABLE IF NOT EXISTS users (
  id_user INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  role ENUM('admin','petugas','dokter','kasir') NOT NULL,
  status ENUM('aktif','nonaktif') NOT NULL DEFAULT 'aktif'
);

INSERT INTO users (username, password, role, status) VALUES
('admin', 'admin', 'admin', 'aktif'),
('petugas', 'petugas', 'petugas', 'aktif'),
('dokter', 'dokter', 'dokter', 'aktif'),
('kasir', 'kasir', 'kasir', 'aktif')
ON DUPLICATE KEY UPDATE role = VALUES(role), status = VALUES(status);

CREATE TABLE IF NOT EXISTS poli (
  id_poli VARCHAR(10) PRIMARY KEY,
  nama_poli VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS pasien (
  id_pasien VARCHAR(10) PRIMARY KEY,
  nik VARCHAR(30) NOT NULL,
  nama_pasien VARCHAR(100) NOT NULL,
  jenis_kelamin VARCHAR(20) NOT NULL,
  tanggal_lahir DATE NOT NULL,
  alamat TEXT NOT NULL,
  no_hp VARCHAR(30) NOT NULL,
  alergi TEXT
);

CREATE TABLE IF NOT EXISTS dokter (
  id_dokter VARCHAR(10) PRIMARY KEY,
  nama_dokter VARCHAR(100) NOT NULL,
  id_poli VARCHAR(10) NOT NULL,
  no_hp VARCHAR(20),
  alamat TEXT,
  status VARCHAR(30) NOT NULL,
  CONSTRAINT fk_dokter_poli FOREIGN KEY (id_poli) REFERENCES poli(id_poli)
);

CREATE TABLE IF NOT EXISTS jadwal_dokter (
  id_jadwal VARCHAR(10) PRIMARY KEY,
  id_dokter VARCHAR(10) NOT NULL,
  id_poli VARCHAR(10) NOT NULL,
  hari VARCHAR(20) NOT NULL,
  jam_mulai VARCHAR(10) NOT NULL,
  jam_selesai VARCHAR(10) NOT NULL,
  kuota INT NOT NULL,
  CONSTRAINT fk_jadwal_dokter FOREIGN KEY (id_dokter) REFERENCES dokter(id_dokter),
  CONSTRAINT fk_jadwal_poli FOREIGN KEY (id_poli) REFERENCES poli(id_poli)
);

CREATE TABLE IF NOT EXISTS obat (
  id_obat VARCHAR(10) PRIMARY KEY,
  nama_obat VARCHAR(100) NOT NULL,
  satuan VARCHAR(30) NOT NULL,
  harga DECIMAL(12,2) NOT NULL,
  stok_awal INT NOT NULL DEFAULT 0,
  stok_masuk INT NOT NULL DEFAULT 0,
  stok_retur INT NOT NULL DEFAULT 0,
  stok_akhir INT NOT NULL DEFAULT 0,
  tanggal_expired DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS kunjungan (
  no_kunjungan VARCHAR(30) PRIMARY KEY,
  id_pasien VARCHAR(10) NOT NULL,
  id_dokter VARCHAR(10) NOT NULL,
  id_poli VARCHAR(10) NOT NULL,
  tanggal_kunjungan DATE NOT NULL,
  keluhan TEXT NOT NULL,
  diagnosa TEXT,
  tindakan TEXT,
  status VARCHAR(30) NOT NULL,
  CONSTRAINT fk_kunjungan_pasien FOREIGN KEY (id_pasien) REFERENCES pasien(id_pasien),
  CONSTRAINT fk_kunjungan_dokter FOREIGN KEY (id_dokter) REFERENCES dokter(id_dokter),
  CONSTRAINT fk_kunjungan_poli FOREIGN KEY (id_poli) REFERENCES poli(id_poli)
);

CREATE TABLE IF NOT EXISTS resep (
  id_resep VARCHAR(10) PRIMARY KEY,
  no_kunjungan VARCHAR(30) NOT NULL,
  tanggal_resep DATE NOT NULL,
  catatan TEXT,
  CONSTRAINT fk_resep_kunjungan FOREIGN KEY (no_kunjungan) REFERENCES kunjungan(no_kunjungan)
);

CREATE TABLE IF NOT EXISTS resep_detail (
  id_resep_detail VARCHAR(10) PRIMARY KEY,
  id_resep VARCHAR(10) NOT NULL,
  id_obat VARCHAR(10) NOT NULL,
  jumlah INT NOT NULL,
  aturan_pakai VARCHAR(100) NOT NULL,
  harga_satuan DECIMAL(12,2) NOT NULL,
  subtotal DECIMAL(12,2) NOT NULL,
  CONSTRAINT fk_resep_detail_resep FOREIGN KEY (id_resep) REFERENCES resep(id_resep),
  CONSTRAINT fk_resep_detail_obat FOREIGN KEY (id_obat) REFERENCES obat(id_obat)
);

CREATE TABLE IF NOT EXISTS pembayaran (
  id_pembayaran VARCHAR(10) PRIMARY KEY,
  no_kunjungan VARCHAR(30) NOT NULL,
  tanggal_pembayaran DATE NOT NULL,
  biaya_konsultasi DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  biaya_tindakan DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  biaya_obat DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  total_tagihan DECIMAL(12,2) NOT NULL,
  jumlah_bayar DECIMAL(12,2) NOT NULL,
  kembalian DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  metode_pembayaran VARCHAR(30) NOT NULL,
  status_pembayaran VARCHAR(30) NOT NULL,
  CONSTRAINT fk_pembayaran_kunjungan FOREIGN KEY (no_kunjungan) REFERENCES kunjungan(no_kunjungan)
);
