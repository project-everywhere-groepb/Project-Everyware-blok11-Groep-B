# Project-Everyware-blok11-Groep-B

Project Everywere Blok 11 Avans.
Project wordt een mobiele app en webapp gemaakt voor video bewerking van sporters en coaches voor verbetering.

# GitLab flow
- Feature branches gebruikt worden, geen directe commits naar master gedaan worden
- Alle commits worden getest, niet alleen naar master
- Run alle tests op iedere commit
- Code reviews worden gedaan voordat er met de main branch gemerged wordt, niet naderhand
- Automatische deployment
- Releases zijn gebaseerd op tags
- Geen rollbacks maar roll forward, gepushte commits worden niet terug gerold
- Iedere feature start vanaf master en target master
- Bugs in master moeten eerst gefixt worden voordat er aan nieuwe features gewerkt wordt
- Commit messages geven een intent aan

# Vóór het maken van een pullrequest:
1) Merge master in je branch
2) Run de linter
3) Alle tests moeten lokaal runnen
4) Het moet kunnen builden

# Vóór het goedkeuren van een pullrequest:
1) Check de desbetreffende branch
2) Build de branch
3) Run de tests op jouw machine
4) Test de features

# Coding guidelines:
**Voor alle projecten**
Classes zijn CamelCase
```java
  public class MyNewClass() {
  }
```
Methods en variablen zijn camelCase
Hanteer leesbare variabelen en class names

**Voor Android**
Standaard Android studio linter wordt gebruikt met de default settings

**Voor Webaplicatie**
ESLint wordt gebruikt met AirBNB presets
