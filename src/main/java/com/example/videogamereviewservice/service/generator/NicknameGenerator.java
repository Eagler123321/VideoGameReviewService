package com.example.videogamereviewservice.service.generator;

import com.example.videogamereviewservice.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.List;

@Component
public class NicknameGenerator {
    private static final List<String> ADJECTIVES = List.of(
            "Night", "Fat", "Pro", "Shadow", "Dark", "Epic", "Mega", "Ultra",
            "Silent", "Wild", "Crazy", "Lazy", "Angry", "Happy", "Cool", "Fast"
    );

    private static final List<String> NOUNS = List.of(
            "Killer", "Lion", "Wolf", "Sniper", "Dragon", "Titan", "Ninja",
            "Warrior", "Ghost", "Phoenix", "Viper", "Bear", "Eagle", "Shark"
    );

    private static final List<String> MLG_PREFIXES = List.of("XxX_", "xX_", "_xX");
    private static final List<String> MLG_SUFFIXES = List.of("_xXx", "xX", "xX_");
    private static final List<String> MEME_FACES = List.of(
            "( ͡° ͜ʖ ͡°)", "( ͡~ ͜ʖ ͡~)", "ಠ_ಠ", "(•̀ᴗ•́)و", "¯\\_(ツ)_/¯"
    );

    private final SecureRandom random = new SecureRandom();

    private <T> T getRandom(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    private String generateMlg(String adj, String noun, String numbers) {
        String prefix = getRandom(MLG_PREFIXES);
        String suffix = getRandom(MLG_SUFFIXES);
        return prefix + adj + noun + "_" + numbers + suffix;
    }

    private String generateMeme(String adj, String noun, String numbers) {
        String face = getRandom(MEME_FACES);
        return adj + noun + numbers + "_" + face;
    }

    private String generateRandom(String adj, String noun, String numbers) {
        int style = random.nextInt(4);
        return switch (style) {
            case 0 -> adj + noun + numbers;
            case 1 -> generateMlg(adj, noun, numbers);
            case 2 -> generateMeme(adj, noun, numbers);
            case 3 -> noun + adj + numbers;
            default -> adj + noun + numbers;
        };
    }

    public String generate(String style) {
        String adj = getRandom(ADJECTIVES);
        String noun = getRandom(NOUNS);
        String numbers = String.format("%03d", random.nextInt(1000));

        return switch (style) {
            case "mlg" -> generateMlg(adj, noun, numbers);
            case "meme" -> generateMeme(adj, noun, numbers);
            case "simple" -> adj + noun + numbers;
            default -> generateRandom(adj, noun, numbers);
        };
    }

    public String generateUnique(String style, UserRepository userRepository) {
        String nickname;
        int attempts = 0;
        do {
            nickname = generate(style);
            attempts++;
            if (attempts > 100) {
                nickname = generate(style) + "_" + random.nextInt(10000);
                break;
            }
        } while (userRepository.existsByNickname(nickname));

        return nickname;
    }
}
