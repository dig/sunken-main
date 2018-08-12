package me.minigames.command;

import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
public class CommandSpecimen {
    @Getter
    private CommandSender sender;
    @Getter
    private String[] args;

    public String concatString(int fromIndex, String delimiter) {
        Joiner joiner = Joiner.on(delimiter);
        return joiner.join(Arrays.copyOfRange(args, fromIndex, args.length - 1));
    }

    public Optional<Integer> getInteger(String str) {
        try {
            return Optional.of(Integer.parseInt(str));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
