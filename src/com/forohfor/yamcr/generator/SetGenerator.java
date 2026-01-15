package com.forohfor.yamcr.generator;

import com.forohfor.yamcr.config.SavedConfig;
import com.forohfor.yamcr.recognition.ListRecogStrat;
import com.forohfor.yamcr.service.ProgressTracker;
import com.forohfor.yamcr.util.CardUtils;
import forohfor.scryfall.api.Card;
import forohfor.scryfall.api.MTGCardQuery;
import forohfor.scryfall.api.Set;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SetGenerator
{

    public static final String[] SET_TYPES =
            {
                    "all",
                    "expansion+core",
                    "core",
                    "expansion",
                    "masters",
                    "masterpiece",
                    "from_the_vault",
                    "premium_deck",
                    "duel_deck",
                    "commander",
                    "planechase",
                    "conspiracy",
                    "archenemy",
                    "vanguard",
                    "funny",
                    "starter",
                    "box",
                    "promo",
                    "token",
                    ""
            };

    /**
     * Generate sets based on set type filter.
     * 
     * @param setType Type filter (e.g., "all", "expansion", "core", "expansion+core", etc.)
     * @param tracker Progress tracker for reporting generation progress
     */
    public static void bulkGenSets(String setType, ProgressTracker tracker)
    {
        if (setType == null || setType.isEmpty())
        {
            setType = "all";
        }

        writeSets(setType, tracker);
    }

    private static void writeSets(String selectedType, ProgressTracker tracker)
    {
        ArrayList<Set> sets = MTGCardQuery.getSets();
        ArrayList<Set> toGenerate = new ArrayList<Set>();

        for (Set set : sets)
        {
            String setType = set.getSetType();
            if ("expansion+core".equals(selectedType))
            {
                if (!(setType.equals("core") || setType.equals("expansion")))
                {
                    continue;
                }
            } else if (!selectedType.equals("all"))
            {
                if (!setType.equals(selectedType))
                {
                    continue;
                }
            }

            String path = SavedConfig.getSetPath(set.getCode());
            File f = new File(path);

            if ((f.exists() && f.isFile()))
            {
                int size = ListRecogStrat.getSizeFromFile(f);
                if (size != set.getCardCount())
                {
                    toGenerate.add(set);
                }
            } else
            {
                toGenerate.add(set);
            }
        }
        generateSets(toGenerate, tracker);
    }

    /**
     * Generate multiple sets with progress tracking.
     * 
     * @param sets List of sets to generate
     * @param tracker Progress tracker for reporting generation progress
     */
    public static void generateSets(List<Set> sets, ProgressTracker tracker)
    {
        if (tracker.startTask("Generating Sets", sets.size()))
        {
            for (Set set : sets)
            {
                tracker.setSubtaskName(set.getName());
                generateSet(set, tracker);
                tracker.progressTask();
            }
            tracker.completeTask();
        }
    }

    public static boolean generateSet(Set set)
    {
        return generateSet(set, new com.forohfor.yamcr.service.NoOpProgressTracker());
    }

    public static boolean generateSet(Set set, ProgressTracker tracker)
    {
        String path = SavedConfig.getSetPath(set.getCode());
        ListRecogStrat r = new ListRecogStrat(set.getName());
        r.setSetSize(set.getCardCount());
        File f = new File(path);

        ArrayList<Card> cards = MTGCardQuery.getCardsFromURI(set.getSearchURI());

        int i = 0;
        for (Card card : cards)
        {
            i++;

            if (CardUtils.isEssentialBasic(card.getName()))
            {
                if (!(SavedConfig.WRITE_BASICS_TO_SETS || card.isFullArt()))
                {
                    continue;
                }
            }
            tracker.setSubtaskName(String.format("(%d / %d) %s", i, set.getCardCount(), card.getName()));
            r.addFromCard(card);
        }

        try
        {
            r.writeOut(f);
            return true;
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }


}
