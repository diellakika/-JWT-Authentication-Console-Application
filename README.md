# Sistemi i Autentifikimit me JWT në Java

Ky projekt implementon një mekanizëm të thjeshtë autentifikimi duke përdorur **JSON Web Tokens (JWT)** në Java. Ai është i ndarë në tre komponentë kryesorë:

- **Serveri** – Pranon kërkesa, verifikon kredencialet dhe gjeneron token JWT.
- **Klienti** – Dërgon kredencialet e autentifikimit te serveri.
- **Shërbimi i Përdoruesit** – Verifikon kredencialet me vlera të hardcoduara.

---

## 📦 Kërkesat

- Java JDK 10 ose më i ri
- Maven (për menaxhimin e varësive)
- Varësia: [`com.auth0:java-jwt`](https://github.com/auth0/java-jwt)

---

## 🚀 Si të Ekzekutohet

### 1. Nisja e Serverit

```bash
javac server/*.java
java server.Server
```

Serveri do të nisë në portin `8080` dhe do të presë lidhje nga klientët.

### 2. Nisja e Klientit (në një terminal tjetër)

```bash
javac client/*.java
java client.Client
```

### 3. Autentifikimi

Kur të kërkohet, përdorni:

- **Emri i përdoruesit:** `admin`
- **Fjalëkalimi:** `password123`

---

## 🧩 Komponentët

### `Server.java`

- Dëgjon në portin `8080`
- Krijon një **thread** të ri për secilin klient
- Kërkon dhe verifikon kredencialet përmes `UserService`
- Nëse janë të sakta, gjeneron një **token JWT** të vlefshëm për 1 orë (përdor RSA)


### `Client.java`

- Lidhet me `localhost:8080`
- Merr kredencialet nga përdoruesi përmes tastierës
- I dërgon te serveri dhe shfaq rezultatin (token ose dështim)

### `UserService.java`

- Përmban logjikën e verifikimit
- Verifikon kundër vlerave të hardcoduara:
  - **admin** / **password123**

### `JwtUtil.java`

- Gjeneron tokenat JWT
- Përdor algoritmin **RSA** për nënshkrim
- Tokeni përfshin:
  - Subjektin (emrin e përdoruesit)
  - Kohën e krijimit
  - Kohën e skadimit (1 orë)

---

## ✅ Shembuj Ekzekutimi

### ✔️ Autentifikim i suksesshëm

**Serveri:**

```
Përdoruesi 'admin' u autentifikua me sukses.
Tokeni u dërgua te klienti.
```

**Klienti:**

```
Autentifikimi i suksesshëm!
JWT Token: <token>
```

### ❌ Autentifikim i pasuksesshëm

**Serveri:**

```
Autentifikimi dështoi për përdoruesin.
```

**Klienti:**

```
Autentifikimi dështoi. Kredencialet janë të pasakta.
```

---
Ekziston mundësia për log out kur useri është valid.
> 💡 Ky projekt është krijuar për qëllime edukative dhe demonstron bazat e autentifikimit me JWT në Java.
