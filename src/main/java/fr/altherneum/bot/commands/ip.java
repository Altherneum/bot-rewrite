package fr.altherneum.bot.commands;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class ip {
    private ip() {
    }

    public static void onIP() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                if (event.getName().equalsIgnoreCase("ip")) {
                    try {
                        event.replyEmbeds(commandes()).queue();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }

    public static net.dv8tion.jda.api.entities.MessageEmbed commandes() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setThumbnail(Main.jda.getSelfUser().getEffectiveAvatarUrl());
        embedBuilder.setTitle("📚 IP/DNS d'" + Main.jda.getSelfUser().getName());
        embedBuilder.addField("Adresse : ", "**__Altherneum.fr__**  ou  __**play**__.altherneum.fr", false);
        GuildMessageChannel minecraftInfo = Discord.textChannel(IDs.MinecraftInfo);
        GuildMessageChannel minecraftReboot = Discord.textChannel(IDs.MinecraftReboot);
        GuildMessageChannel minecraftUptime = Discord.textChannel(IDs.MinecraftUptime);
        GuildMessageChannel minecraftMAJ = Discord.textChannel(IDs.MinecraftMAJ);

        embedBuilder.addField("Salons : ", minecraftInfo.getAsMention() + "\n" + minecraftReboot.getAsMention() + "\n"
                + minecraftUptime.getAsMention() + "\n" + minecraftMAJ.getAsMention(), false);

        String string = "";
        for (Member member : Discord.guild().getMembers()) {
            if (member.getRoles().contains(IDs.RoleBotsMC)) {
                string += "\n" + member.getUser().getAsMention();
            }
        }
        embedBuilder.addField("Serveurs : ", string, false);

        return embedBuilder.build();
    }
}
