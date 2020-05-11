#Ausgangslage#
Jeder Benutzer kann sich mit seinem eigenen Login-Daten anmelden.
Pro Benutzer wird ein Tabelleneintrag mit Benutzername und PW(gehasht) gespeichert in der DB.
Ein Benutzer kann x beliebig viele Passwörter speichern.
Pro Passwort wird die Domain, Der Username und das Passwort(gehasht + Salt von UserPW)von der Seite in der DB gespeichert.

###Salt & Hashing###
Damit wir den Salt für die einzelnen Domainpasswörter erstellen können, brauchen wir jedoch das Masterpasswort im Klartext. Deshalb speichern wir die Credentials temporär in der Applikation.
Da wir zwar das Passwort und den Benutzernamen in der Applikation speichern, ist dies dennoch nicht sehr problematisch.
Die Applikation führt dies pro Benutzer individuell durch, da dies im Rahmen vom Sessionscope geschieht.

###Masterpasswort###

Wenn der Benutzer sein eigenes Login Passwort(Master Passwort) verliert, gibt es von unserer Seite her keine Möglichkeit, dies wiederherzustellen,
da wir das PW nur zur Laufzeit verschlüsseln und gehasht abspeichern.


![GitHub Logo](https://media.discordapp.net/attachments/511620593859428353/709327315552960523/QuickPlanning.PNG)