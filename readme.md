# Systém pro městskou knihovnu
 
<br>

**Zpráva o projektu**


Jan Bajer & Dinh Dinh Truong
 
<br>


**Popis aplikace**
<br>
Účelem systému je zjednodušit správu knihovny a vytvořit snadno použitelnou aplikaci pro uživatele k půjčování a prohlížení dostupných knih.
Dále by měl systém pomoc knihovníkům při správě knih vlastněné knihovnou. Systém je založen na relační databázi, která znázorňuje správu knih a rezervační funkce.
Aplikace má 2 druhy uživatelů: “guest” – přihlášený uživatel a “librarian” – knihovník. Uživatelé jsou čtenáři, kteří si mohou zarezervovat knihu. Knihovníci jsou správci knihovny, kteří mohou vytvořit účet dalšímu knihovníkovi, mohou aktualizovat a přidávat nový obsah knihovně.

**Struktura**
<br>
Projekt je složen ze 3. částí – databáze, server a klientská strana.

**Databáze**
<br>
Je použitá PostgreSQL a spojení je definováno v src/main/resources/application.properties.

**Klientská strana**
<br>
Pro otestování aplikace byly vytvořeny testy, nicméně hlavní funkcionality byly testovány přes aplikaci Postman. Frontend aplikace není vytvořen.

**Server**
<br>
Server je Java Spring aplikace. Zajišťuje veškerou komunikaci s databází a obstarává všechny požadavky od klientů. Defaultně běží na localhost:8080/implementation.


**Návod jak naintalovat aplikaci**
<br>
1. Stáhněte projekt
2. Otevřete projekt v IntelliJ IDEA
3. (Není nutné!) Pokud chcete změnit databázi nebo připojení, tak v src/main/resources/application.properties můžete změnit URL, username a password.
4. Buildněte projekt s Mavenem
5. Spustitelný soubor library.jar najdete v /target adresáři
6. Spustit aplikaci
7. (Chybí FRONTEND!) Otevřít: http://localhost:8080/implementation/login 

**Problémy**
1. Při GET pro např. getUserReservations se vygenerovaná JSON data zacyklí, při generování dat pro book

**Co jsme se naučili**

**Jan Bajer**
<br>
Práci se Springem hodnotím kladně, protože mě naučila spoustu nových věcí. Prohloubil jsem si své znalosti v Javě. Předmět mi ukázal přehled architektur enterprise informačních systémů. Konkrétně technologie Spring, JPA a REST webové služby.

**Pavel Truong**
<br>
Práci se Springem beru jako velice naučnou zkušenost a věřím, že ji určitě v budoucnu využiji v praxi. 


