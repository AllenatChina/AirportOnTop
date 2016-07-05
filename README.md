# AirportOnTop
Yeah, AirportOnTop!

# To Run on your computer:

1. Install **PostgreSQL** on your computer
2. Add a database called "**airportDB**" in PostgreSQL
3. Restore the data from the file "**airportDB.backup**" into the "airportDB" database (through pgAdmin or whatever you like)
4. Install **Intellij Idea** on your computer
5. Download my repository and import it into Intellij as a Java project
6. Find **/src/main/resources/ontology/AirportProtege.obda** 
7. Change the contents under **"[SourceDeclaration]"** in AirportProtege.obda to the **connectionUrl, username, password** of your **own database** (Default is mine)
8. Build and Run AirportOnTop.main()

# Bad things that may happen

- Your computer **doesn't have Java installed**, you can't do anything then
- Your computer **doesn't have Internet connected**, you can't build the Java files I guess
- Your computer is **Windows 10** operating system, anything can happen
- You don't read English...