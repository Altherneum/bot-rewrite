# bot-rewrite

Migration du bot Discord **Altherneum** de [JavaCord](https://github.com/Javacord/Javacord)
(archivé et déprécié) vers [JDA](https://github.com/discord-jda/JDA).

Le comportement visible est conservé au maximum : mêmes commandes, mêmes
messages, mêmes embeds, mêmes actions et mêmes événements que le bot d'origine.
Seules les incompatibilités imposées par JDA/Discord ont été adaptées.

## Stack

- Java 21
- Maven
- JDA `6.4.1`
- `org.json` pour les données locales
- SnakeYAML uniquement pour relire `/DiscordBot/data/tokens.yml`
- Fat jar via `maven-shade-plugin`

## Compilation

```bash
mvn -B package
```

Le jar exécutable est produit ici :

```text
target/bot-rewrite-1.0-fat.jar
```

Lancement :

```bash
java -jar target/bot-rewrite-1.0-fat.jar
```

Mode test sans actions de démarrage (pas de message d'online, pas de mise à
jour des messages modèles, pas de timers stats/tops/banqueroute) :

```bash
java -jar target/bot-rewrite-1.0-fat.jar --safe
```

## Configuration du token

Ordre de priorité, identique à l'ancien bot :

1. variable d'environnement `DISCORD_BOT_TOKEN` (ou
   `DISCORD_TEST_BOT_TOKEN` pour le compte de test) ;
2. fichier `/DiscordBot/data/tokens.yml` :

```yaml
Bot: "TOKEN_DU_BOT"
TestBot: "TOKEN_DU_BOT_DE_TEST"
```

Le token peut être entouré de guillemets, précédé de `Bot ` ou contenir des
retours chariot : il est nettoyé automatiquement.

## Données locales

Les données sont maintenant en JSON dans `Data/` (créé au premier lancement) :

| Fichier | Contenu |
|---|---|
| `Data/<userId>.json` | or, messages, vocal, invitations, réputation, gains passifs, dates de jeu |
| `Data/globalInvites.json` | liste globale des utilisateurs déjà comptés |
| `Data/event.json` | état du concours d'invitations |
| `Data/BanqueRoute.json` | cagnotte de la banque |

Les clés internes de l'ancien bot sont conservées (`Gold`, `Messages`,
`voiceTime`, `InvitesCounted.<code>`, etc.).

## Commandes

Les 19 commandes slash et le menu contextuel utilisateur `profil` sont
enregistrés au démarrage :

`emptychannel`, `test`, `ping`, `profil`, `mute`, `unmute`, `kick`,
`addreaction`, `ban`, `unban`, `copymessage`, `createinvite`, `help`,
`coinflip`, `createbutton`, `ip`, `banqueroute`, `upvote`, `yt`.

Les IDs du serveur, salons et rôles sont dans
`src/main/java/fr/altherneum/bot/config/IDs.java`.

## Différences volontaires

- `/ban` ne tente plus une seconde réponse d'interaction : Discord n'autorise
  qu'une seule réponse. La réponse visible (bouclier) est conservée.
- Le retrait de réaction dans les salons de rôles ne retire pas le rôle, comme
  dans l'ancien bot (la condition d'origine comparait un `String` à un objet
  `ChannelCategory` et était donc toujours fausse). Ce comportement est
  volontairement conservé pour rester identique.
- Le message « Le bot est reconnecté » n'est plus envoyé à chaque reprise de
  session de la passerelle : Discord demande régulièrement au bot de se
  reconnecter (opcode 7) pour rééquilibrer ses connexions, et JDA rejoue les
  événements manqués sans que le bot soit réellement hors ligne. Le message
  n'apparaît désormais que si la coupure a duré au moins 5 minutes.
- Le stockage passe de YAML (Bukkit/Paper) à JSON, et la dépendance Paper a
  été supprimée.

## Migration depuis l'ancien bot

- Les salons **texte** et **annonces** (`GUILD_NEWS`) sont acceptés partout où
  JavaCord exposait un `ServerTextChannel`. JDA les sépare en `TextChannel` et
  `NewsChannel` ; le bot utilise leur supertype commun.
- Si le nouveau bot utilise une **autre application Discord** que l'ancien, il
  ne peut pas modifier les messages déjà envoyés par l'ancien compte bot.
  Ces messages sont ignorés avec un log `[messageUpdater] ... ignoré` et le
  démarrage continue. Pour conserver la mise à jour automatique des messages
  modèles, utiliser le même token que l'ancien bot ou recréer ces messages avec
  le nouveau bot.
- Le message d'activation (`Le bot est en ligne`) et les autres nouveaux
  messages fonctionnent quel que soit le compte bot utilisé.

## Structure

```text
src/main/java/fr/altherneum/bot/
├── Main.java                 # point d'entrée
├── api/                      # messages, logs, vérifications, rôles
├── commands/                 # commandes slash et menu contextuel
├── config/                   # IDs du serveur et chargement du token
├── events/                   # démarrage et reconnexion
├── listeners/                # événements Discord, casino, stats, tickets...
└── utils/                    # JDA, JSON, cache de messages, fichiers
```

## Dépôts

- Bot d'origine (JavaCord) : <https://github.com/Altherneum/bot>
- Exemple JDA : <https://github.com/Altherneum/bot-jda>
- Documentation JDA : <https://jda.wiki/>
