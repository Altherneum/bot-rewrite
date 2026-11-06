package fr.altherneum.bot.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;

public final class getYT {
    public static List<String> youtubeUrls = new ArrayList<>();
    public static StringBuffer nonMatchingText = new StringBuffer();

    private getYT() {
    }

    public static void onYT() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                if (event.getName().equalsIgnoreCase("yt")) {
                    try {
                        String id = event.getOption("channel_id").getAsString();
                        int number = (int) Math.round(event.getOption("amount").getAsDouble());

                        event.reply("\uD83D\uDC8E").queue();
                        new Thread(() -> {
                            try {
                                dumpVideoFromChannel(id, number);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }).start();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }

    public static void dumpVideoFromChannel(String channelID, int number) throws IOException {
        GuildMessageChannel serverTextChannel = Discord.textChannel(channelID);

        int count = 0;
        int total = 0;
        for (Message message : lastMessages(serverTextChannel, number)) {
            String messageText = message.getContentRaw();
            total++;
            if (messageText.contains("https://www.youtube.com/") || messageText.contains("https://youtube.com/")
                    || messageText.contains("https://youtu.be/") || messageText.contains("https://youtube.com/shorts/")
                    || messageText.contains("https://music.youtube.com/")) {
                checkForYoutubeMessage(messageText);
                message.delete().complete();
                count++;
            }

            if (total % 100 == 0) {
                serverTextChannel.sendMessage("n°" + total).complete();
            }

            if (count >= 10) {
                count = 0;
                if (youtubeUrls.size() >= 1) {
                    File tempFile = new File("output.txt");
                    try (FileWriter writer = new FileWriter(tempFile)) {
                        writer.write(youtubeUrls.toString());
                    }

                    serverTextChannel.sendFiles(FileUpload.fromData(tempFile))
                            .setContent("Here is the file containing the string:")
                            .complete();
                    youtubeUrls = new ArrayList<>();
                }

                if (nonMatchingText.length() >= 1) {
                    File tempFile2 = new File("output2.txt");
                    try (FileWriter writer = new FileWriter(tempFile2)) {
                        writer.write(nonMatchingText.toString());
                    }

                    serverTextChannel.sendFiles(FileUpload.fromData(tempFile2))
                            .setContent("Here is the file containing the string:")
                            .complete();

                    nonMatchingText = new StringBuffer();
                }
            }
        }

        if (youtubeUrls.size() >= 1) {
            File tempFile = new File("output.txt");
            try (FileWriter writer = new FileWriter(tempFile)) {
                writer.write(youtubeUrls.toString());
            }

            serverTextChannel.sendFiles(FileUpload.fromData(tempFile))
                    .setContent("Here is the file containing the last string:")
                    .complete();
            youtubeUrls = new ArrayList<>();
        }

        if (nonMatchingText.length() >= 1) {
            File tempFile2 = new File("output2.txt");
            try (FileWriter writer = new FileWriter(tempFile2)) {
                writer.write(nonMatchingText.toString());
            }

            serverTextChannel.sendFiles(FileUpload.fromData(tempFile2))
                    .setContent("Here is the file containing the last string:")
                    .complete();

            nonMatchingText = new StringBuffer();
        }

        serverTextChannel.sendMessage("OK, ended loop").complete();
    }

    private static List<Message> lastMessages(GuildMessageChannel channel, int number) {
        List<Message> messages = new ArrayList<>(channel.getIterableHistory().stream().limit(number).toList());
        Collections.reverse(messages);
        return messages;
    }

    public static void checkForYoutubeMessage(String text) {
        Pattern pattern = Pattern.compile("((http(s)?://)?)(www\\.|music\\.)?((youtube\\.com\\/)|(youtu\\.be\\/))[\\S]+");

        Matcher matcher = pattern.matcher(text);
        int lastEnd = 0;

        while (matcher.find()) {
            youtubeUrls.add(matcher.group());
            nonMatchingText.append(text.substring(lastEnd, matcher.start()));
            lastEnd = matcher.end();
        }

        nonMatchingText.append(text.substring(lastEnd));
    }
}
