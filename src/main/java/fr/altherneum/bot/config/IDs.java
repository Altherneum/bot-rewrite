package fr.altherneum.bot.config;

import java.util.ArrayList;

import fr.altherneum.bot.Main;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;

/**
 * Identifiants et constantes du serveur Altherneum.
 *
 * <p>Les IDs sont strictement les mêmes que dans l'ancien bot JavaCord.
 */
public final class IDs {
    public static final String MessageRoleGain = " Vous avez **__reçu le rôle__** : ";
    public static final String MessageRolePerte = " Vous avez **__perdu le rôle__** : ";

    public static final Category CategoryAltherneum = Main.jda.getCategoryById("1081936131542761634");
    public static final Category CategoryTicket = Main.jda.getCategoryById("1081946605680328874");
    public static final Category CategoryRoles = Main.jda.getCategoryById("1082074221871644824");
    public static final int defaultRoleSizeWithSplitterAndRulesAndEveryOne =
            rolesSplitterList().size() + 1 + 1;

    public static final Role RoleEveryone = Main.jda.getGuildById("1081921426333909072").getPublicRole();
    public static final Role RoleBotsMC = Main.jda.getRoleById("1081965950905098310");

    public static final Role RoleReglesValides = Main.jda.getRoleById("1081964412480528464");
    public static final Role RoleCasinoBypass = Main.jda.getRoleById("1081955127667212439");
    public static final Role RoleFondateur = Main.jda.getRoleById("1081954508998979625");
    public static final Role RoleDirection = Main.jda.getRoleById("1081954653949923488");
    public static final Role RoleModeration = Main.jda.getRoleById("1081954943491117256");
    public static final Role RoleAssistance = Main.jda.getRoleById("1081954923861790800");

    public static final Role RoleVIP = Main.jda.getRoleById("1081955467623944292");
    public static final Role RoleBoost = Main.jda.getRoleById("1081955280490876948");
    public static int prixVIP = 100_000;

    public static final Role RoleTwitter = Main.jda.getRoleById("1081965199382302842");
    public static final Role RoleVideoLive = Main.jda.getRoleById("1081965222669074512");
    public static final Role RoleInstagram = Main.jda.getRoleById("1081965241837031495");
    public static final Role RoleMiseAJour = Main.jda.getRoleById("1081965280818905239");
    public static final Role RoleEvents = Main.jda.getRoleById("1081965300339191818");
    public static final Role RoleAnnonces = Main.jda.getRoleById("1081965259939647508");
    public static final Role RoleTournois = Main.jda.getRoleById("1081965322694828052");

    public static final Role RoleGenshinImpact = Main.jda.getRoleById("1081964512049102858");
    public static final Role RoleGTAV = Main.jda.getRoleById("1081964533612019752");
    public static final Role RoleFortnite = Main.jda.getRoleById("1081964556915589151");
    public static final Role RoleCSGO = Main.jda.getRoleById("1081964578692411422");
    public static final Role RoleLeagueOfLegends = Main.jda.getRoleById("1081964599877836893");
    public static final Role RoleAlbionOnline = Main.jda.getRoleById("1081964685496156260");
    public static final Role RoleShopTitans = Main.jda.getRoleById("1081964836688244786");
    public static final Role RoleValorant = Main.jda.getRoleById("1081964620207624242");
    public static final Role RoleRocketLeague = Main.jda.getRoleById("1081964463906893916");
    public static final Role RoleSuperCell = Main.jda.getRoleById("1081964487822803094");
    public static final Role RoleRoblox = Main.jda.getRoleById("1115390280879837276");

    public static final Role RolePC = Main.jda.getRoleById("1081965472720900147");
    public static final Role RoleTelephone = Main.jda.getRoleById("1081965501498007582");
    public static final Role RoleXBox = Main.jda.getRoleById("1081965530304483379");
    public static final Role RolePS = Main.jda.getRoleById("1081965549392769034");
    public static final Role RoleSwitch = Main.jda.getRoleById("1081965568808198255");

    public static final Role RoleHomme = Main.jda.getRoleById("1081965387320676442");
    public static final Role RoleFemme = Main.jda.getRoleById("1081965406962581595");
    public static final Role RoleMineur = Main.jda.getRoleById("1081965427065880617");
    public static final Role RoleMajeur = Main.jda.getRoleById("1081965448314245193");

    public static final Role Roleinvites100 = Main.jda.getRoleById("1081955509223043072");
    public static final Role Roleinvites50 = Main.jda.getRoleById("1081955554018209892");
    public static final Role Roleinvites30 = Main.jda.getRoleById("1081955596988846170");
    public static final Role Roleinvites10 = Main.jda.getRoleById("1081955638114000956");
    public static final Role Rolemsg1000 = Main.jda.getRoleById("1081955531494785139");
    public static final Role Rolemsg200 = Main.jda.getRoleById("1081955574528352366");
    public static final Role Rolemsg50 = Main.jda.getRoleById("1081955618954420335");
    public static final Role Rolemsg10 = Main.jda.getRoleById("1081955657856581662");

    public static final String serverID = "1081921426333909072";

    public static final String MinecraftInfo = "1092924495368564777";
    public static final String MinecraftReboot = "1081945572744892466";
    public static final String MinecraftUptime = "1093591413159112847";
    public static final String MinecraftMAJ = "1093593961597259817";

    public static final String Notifications = "1082075867037061210";
    public static final String Profil = "1082076166158037104";
    public static final String AutresJeux = "1082075634374807562";

    public static final String Boost = "1081941784252403742";
    public static final String Ticket = "1081946641268998164";
    public static final String LogsCmd = "1081947278132117504";
    public static final String LogsMessage = "1081947413226455163";
    public static final String Annonces = "1081940940945629194";
    public static final String Regles = "1082078579426345011";
    public static final String ReglesNew = "1117916774390829056";
    public static final String Invitation = "1081941748240089158";
    public static final String Sanctions = "1108535295227465798";
    public static final String AutoMod = "1081947188722155601";
    public static final String NotrePub = "1081940336194105525";
    public static final String Informations = "1081932471408529490";
    public static final String Recrutement = "1095841321417179276";

    public static final String Statistiques = "1081940710799978546";
    public static final String Tops = "1081940819235319808";
    public static final String AFKVoice = "1081943448514482246";
    public static final String CasinoTextuelResultat = "1081943163985469511";

    public static final String CasinoBoutique = "1081940864420548668";
    public static final String CasinoDailyClick = "1082775165366177912";
    public static final String CasinoCoinFlip = "1092029640845041694";
    public static final String CasinoBanqueRoute = "1106616355731542151";
    public static final String Casino = "1082775318751891506";

    public static final String LogsTachesStatff = "1081946944689147934";
    public static final String EventsNote = "1111998374158290954";

    public static int prixCoinFlip = 1000;

    public static final String EmojiChargement = "<:chargement:1083509029332078622>";
    public static final String EmojiProfil = "<:profil:1083508928316444694>";
    public static final String EmojiPin = "<:pin:1083508925485289574>";
    public static final String EmojiAlbionOnline = "<:albiononline:1083509077759508572>";
    public static final String EmojiValorant = "<:valorant:1083508856442859601>";
    public static final String EmojiLeagueOfLegends = "<:leagueoflegends:1083508964001583134>";
    public static final String EmojiCSGO = "<:csgo:1083509031823478804>";
    public static final String EmojiFortnite = "<:fortnite:1083509037338996858>";
    public static final String EmojiShopTitans = "<:shoptitans:1086739036984586260>";
    public static final String EmojiRocketLeague = "<:rocketleague:1083508931327963280>";
    public static final String EmojiSuperCell = "<:supercell:1083508844577165392>";
    public static final String EmojiGenshinImpact = "<:genshinimpact:1083509038702145646>";
    public static final String EmojiGTAV = "<:gta5:1083509024751898684>";
    public static final String EmojiXBox = "<:xbox:1083508857982177390>";
    public static final String EmojiPS = "<:ps:1083508929956425729>";
    public static final String EmojiSwitch = "<:switch:1083508846871449680>";

    public static final String EmojiJava = "<:java:1083508962139312158>";
    public static final String EmojiBedrock = "<:bedrock:1083509091827208272>";
    public static final String EmojiMinecraft = "<:Minecraft:1083508972402782218>";
    public static final String EmojiMinecraftHeart = "<:minecraft_heart:1083508974785146902>";

    public static final String EmojiClock = "\uD83D\uDD59";
    public static final String EmojiTicket = "\uD83C\uDFAB";
    public static final String EmojiOne = "1️⃣";
    public static final String EmojiTwo = "2️⃣";
    public static final String EmojiThree = "3️⃣";
    public static final String EmojiFour = "4️⃣";
    public static final String EmojiFive = "5️⃣";
    public static final String EmojiArrowsClockwise = "\uD83D\uDD03";
    public static final String EmojiMage = "\uD83E\uDDD9\u200D♂️";
    public static final String EmojiCrossedSwords = "⚔️";
    public static final String EmojiWindBlowingFace = "\uD83C\uDF2C️";
    public static final String EmojiMovieCamera = "\uD83C\uDFA5";
    public static final String EmojiFramePhoto = "\uD83D\uDDBC️";
    public static final String EmojiDesktop = "\uD83D\uDDA5️";
    public static final String EmojiLoudSpeaker = "\uD83D\uDCE2";
    public static final String EmojiPageWithCurl = "\uD83D\uDCC3";
    public static final String EmojiComputer = "\uD83D\uDCBB";
    public static final String EmojiMobilePhone = "\uD83D\uDCF1";
    public static final String EmojiMaleSign = "♂️";
    public static final String EmojiFemaleSign = "♀️";
    public static final String EmojiUnderage = "\uD83D\uDD1E";
    public static final String EmojiOlderMan = "\uD83D\uDC74";
    public static final String EmojiGameDie = "\uD83C\uDFB2";
    public static final String EmojiSpeechBalloon = "\uD83D\uDCAC";
    public static final String EmojiDiamondShapeWithADotInside = "\uD83D\uDCA0";
    public static final String EmojiKnife = "\uD83D\uDD2A";
    public static final String EmojiVideoGame = "\uD83C\uDFAE";
    public static final String EmojiCalendar = "📆";
    public static final String EmojiCrown = "👑";
    public static final String EmojiMedicalSymbol = "⚕️";
    public static final String EmojiShield = "\uD83D\uDEE1️";
    public static final String EmojiBowAndArrow = "\uD83C\uDFF9";
    public static final String EmojiPersonTippingHand = "\uD83D\uDC81";
    public static final String EmojiGreenCrossMark = "❎";
    public static final String EmojiWhiteCheckMark = "✅";
    public static final String EmojiNewsPaper = "📰";
    public static final String EmojiMoneyWithWings = "💸";
    public static final String EmojiMouseThreeButton = "🖱️";
    public static final String EmojiCoin = "🪙";
    public static final String EmojiBanque = "🏦";
    public static final String EmojiRocket = "🚀";

    private IDs() {
    }

    public static ArrayList<String> voiceChannelsCreator() {
        ArrayList<String> list = new ArrayList<>();
        list.add("1081943513081577512"); // général
        return list;
    }

    public static ArrayList<Role> rolesJeux() {
        ArrayList<Role> list = new ArrayList<>();
        list.add(RoleGenshinImpact);
        list.add(RoleAlbionOnline);
        list.add(RoleShopTitans);
        list.add(RoleRocketLeague);
        list.add(RoleSuperCell);
        list.add(RoleGTAV);
        list.add(RoleFortnite);
        list.add(RoleCSGO);
        list.add(RoleLeagueOfLegends);
        list.add(RoleValorant);
        list.add(RoleRoblox);
        return list;
    }

    public static ArrayList<Role> rolesSplitterList() {
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(Main.jda.getRoleById("1081955686688247960"));
        roles.add(Main.jda.getRoleById("1081965082776449096"));
        roles.add(Main.jda.getRoleById("1081965351367102495"));
        roles.add(Main.jda.getRoleById("1081965587951014009"));
        return roles;
    }
}
