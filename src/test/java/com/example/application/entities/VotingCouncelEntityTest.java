package com.example.application.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VotingCouncelEntityTest {

    @Test
    void withoutAttachedCouncilsTotalsAndDisplayNameAreJustThisCouncelsOwnValues() {
        VotingCouncelEntity centar = councelWith("034Б224", "ЦЕНТАР 2-1", 800);

        assertEquals(800, centar.getTotalNumberOfVoters());
        assertEquals("ЦЕНТАР 2-1", centar.getDisplayName());
    }

    @Test
    void withOneAttachedCouncilTotalsAndDisplayNameIncludeIt() {
        VotingCouncelEntity centar = councelWith("034Б224", "ЦЕНТАР 2-1", 800);
        VotingCouncelEntity licno = councelWith("034Б000", "ЛИЧНО", 1634);
        licno.setPrimaryVotingCouncel(centar);
        centar.getAttachedVotingCouncels().add(licno);

        assertEquals(800 + 1634, centar.getTotalNumberOfVoters());
        assertEquals("ЦЕНТАР 2-1 / ЛИЧНО", centar.getDisplayName());

        // The attached council itself isn't affected - it only reports its own figures.
        assertEquals(1634, licno.getTotalNumberOfVoters());
        assertEquals("ЛИЧНО", licno.getDisplayName());
    }

    @Test
    void withMultipleAttachedCouncilsTotalsAndDisplayNameIncludeAllOfThem() {
        VotingCouncelEntity primary = councelWith("034Б501", "РАДНИЧКИ УНИВЕРЗИТЕТ", 500);
        VotingCouncelEntity odsustvo = councelWith("034Б501/ННН", "ОДСУСТВО", 209);
        VotingCouncelEntity licno = councelWith("034Б000", "ЛИЧНО", 1634);
        odsustvo.setPrimaryVotingCouncel(primary);
        licno.setPrimaryVotingCouncel(primary);
        primary.getAttachedVotingCouncels().add(odsustvo);
        primary.getAttachedVotingCouncels().add(licno);

        assertEquals(500 + 209 + 1634, primary.getTotalNumberOfVoters());
        assertEquals("РАДНИЧКИ УНИВЕРЗИТЕТ / ОДСУСТВО / ЛИЧНО", primary.getDisplayName());
    }

    @Test
    void anAttachedCouncilWithNoVoterCountDoesNotBreakTheTotal() {
        VotingCouncelEntity centar = councelWith("034Б224", "ЦЕНТАР 2-1", 800);
        VotingCouncelEntity licno = councelWith("034Б000", "ЛИЧНО", null);
        licno.setPrimaryVotingCouncel(centar);
        centar.getAttachedVotingCouncels().add(licno);

        assertEquals(800, centar.getTotalNumberOfVoters());
    }

    private static VotingCouncelEntity councelWith(String code, String name, Integer numberOfVoters) {
        VotingCouncelEntity entity = new VotingCouncelEntity();
        entity.setCode(code);
        entity.setName(name);
        entity.setNumberOfVoters(numberOfVoters);
        return entity;
    }
}
