/*
 * Copyright (c) 2009,  Sarah Heckman, Laurie Williams, Dright Ho
 * All Rights Reserved.
 *
 * Permission has been explicitly granted to the University of Minnesota
 * Software Engineering Center to use and distribute this source for
 * educational purposes, including delivering online education through
 * Coursera or other entities.
 *
 * No warranty is given regarding this software, including warranties as
 * to the correctness or completeness of this software, including
 * fitness for purpose.
 *
 *
 * Modifications
 * 20171114 - Ian De Silva - Updated to comply with JUnit 4 and to adhere to
 * 							 coding standards.  Added test documentation.
 */
package edu.ncsu.csc326.coffeemaker;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc326.coffeemaker.exceptions.InventoryException;
import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException;

import static org.junit.Assert.*;

/**
 * Unit tests for CoffeeMaker class.
 *
 * @author Sarah Heckman
 */
public class CoffeeMakerTest {

    /**
     * The object under test.
     */
    private CoffeeMaker coffeeMaker;

    // Sample recipes to use in testing.
    private Recipe recipe1;
    private Recipe recipe2;
    private Recipe recipe3;
    private Recipe recipe4;

    /**
     * Initializes some recipes to test with and the {@link CoffeeMaker}
     * object we wish to test.
     *
     * @throws RecipeException if there was an error parsing the ingredient
     *                         amount when setting up the recipe.
     */
    @Before
    public void setUp() throws RecipeException {
        coffeeMaker = new CoffeeMaker();

        //Set up for r1
        recipe1 = new Recipe();
        recipe1.setName("Coffee");
        recipe1.setAmtChocolate("0");
        recipe1.setAmtCoffee("3");
        recipe1.setAmtMilk("1");
        recipe1.setAmtSugar("1");
        recipe1.setPrice("50");

        //Set up for r2
        recipe2 = new Recipe();
        recipe2.setName("Mocha");
        recipe2.setAmtChocolate("20");
        recipe2.setAmtCoffee("3");
        recipe2.setAmtMilk("1");
        recipe2.setAmtSugar("1");
        recipe2.setPrice("75");

        //Set up for r3
        recipe3 = new Recipe();
        recipe3.setName("Latte");
        recipe3.setAmtChocolate("0");
        recipe3.setAmtCoffee("3");
        recipe3.setAmtMilk("3");
        recipe3.setAmtSugar("1");
        recipe3.setPrice("100");

        //Set up for r4
        recipe4 = new Recipe();
        recipe4.setName("Hot Chocolate");
        recipe4.setAmtChocolate("4");
        recipe4.setAmtCoffee("3");
        recipe4.setAmtMilk("1");
        recipe4.setAmtSugar("1");
        recipe4.setPrice("65");
    }

    /**
     * Test recipeBook and inventory of new CoffeeMaker
     */
    @Test
    public void testInitialCoffeeMaker() {
        for (Recipe recipe : coffeeMaker.getRecipes()) {
            assertNull(recipe);
        }
        assertEquals(coffeeMaker.checkInventory(), new Inventory().toString());
    }

    /**
     * Test adding a recipe to CoffeeMaker recipe book
     */
    @Test
    public void testAddRecipe() {
        coffeeMaker.addRecipe(recipe1);
        coffeeMaker.addRecipe(recipe2);
        coffeeMaker.addRecipe(recipe3);
        assertEquals(coffeeMaker.getRecipes()[0], recipe1);
        assertEquals(coffeeMaker.getRecipes()[1], recipe2);
        assertEquals(coffeeMaker.getRecipes()[2], recipe3);
    }

    /**
     * Test deleting a recipe from CoffeeMaker recipe book
     */
    @Test
    public void testDeleteRecipe() {
        coffeeMaker.addRecipe(recipe1); // position 0
        coffeeMaker.deleteRecipe(0);
        assertNull(coffeeMaker.getRecipes()[0]);
    }

    /**
     * Test editing recipe in CoffeeMaker recipe book
     */
    @Test
    public void testEditRecipe() {
        coffeeMaker.addRecipe(recipe1); // position 0
        assertEquals(coffeeMaker.editRecipe(0, recipe2), "Coffee");

        // same name as recipe1, but recipe&price from recipe2
        Recipe editedRecipe = coffeeMaker.getRecipes()[0];
        assertEquals(editedRecipe.getName(), "Coffee");
        assertEquals(editedRecipe.getAmtChocolate(), 20);
        assertEquals(editedRecipe.getAmtCoffee(), 3);
        assertEquals(editedRecipe.getAmtMilk(), 1);
        assertEquals(editedRecipe.getAmtSugar(), 1);
        assertEquals(editedRecipe.getPrice(), 75);
    }

    /**
     * Test adding ingredients to CoffeeMaker inventory
     *
     * @throws InventoryException when use invalid unit in Inventory class
     */
    @Test
    public void testAddInventory() throws InventoryException {
        Inventory testInventory = new Inventory();

        coffeeMaker.addInventory("2", "3", "4", "5");
        testInventory.addCoffee("2");
        testInventory.addMilk("3");
        testInventory.addSugar("4");
        testInventory.addChocolate("5");
        assertEquals(coffeeMaker.checkInventory(), testInventory.toString());

        coffeeMaker.addInventory("5", "6", "7", "8");
        testInventory.addCoffee("5");
        testInventory.addMilk("6");
        testInventory.addSugar("7");
        testInventory.addChocolate("8");
        assertEquals(coffeeMaker.checkInventory(), testInventory.toString());
    }

    /**
     * Test if checkInventory() return correct string
     *
     * @throws InventoryException when use invalid unit in Inventory class
     */
    @Test
    public void testCheckInventory() throws InventoryException {
        Inventory testInventory = new Inventory();
        assertEquals(coffeeMaker.checkInventory(), testInventory.toString());

        coffeeMaker.addInventory("10", "20", "30", "40");
        testInventory.addCoffee("10");
        testInventory.addMilk("20");
        testInventory.addSugar("30");
        testInventory.addChocolate("40");
        assertEquals(coffeeMaker.checkInventory(), testInventory.toString());
    }

    /**
     * Test purchasing beverage with enough money and ingredients
     */
    @Test
    public void testMakeCoffee() {
        Inventory testInventory = new Inventory();
        coffeeMaker.addRecipe(recipe1); // position 0
        coffeeMaker.addRecipe(recipe2); // position 1
        coffeeMaker.addRecipe(recipe3); // position 2

        assertEquals(25, coffeeMaker.makeCoffee(0, 75));
        testInventory.useIngredients(recipe1);
        assertEquals(coffeeMaker.checkInventory(), testInventory.toString());

        assertEquals(0, coffeeMaker.makeCoffee(2, 100));
        testInventory.useIngredients(recipe3);
        assertEquals(coffeeMaker.checkInventory(), testInventory.toString());
    }

    /**
     * Test purchasing an empty recipe beverage
     */
    @Test
    public void testMakeEmptyRecipeCoffee() {
        Inventory testInventory = new Inventory();

        assertEquals(75, coffeeMaker.makeCoffee(0, 75));
        assertEquals(100, coffeeMaker.makeCoffee(3, 100));
        assertEquals(coffeeMaker.checkInventory(), testInventory.toString());
    }

    /**
     * Test purchasing beverage with not enough ingredients in the inventory
     */
    @Test
    public void testMakeNotEnoughIngredientsCoffee() {
        Inventory testInventory = new Inventory();

        coffeeMaker.addRecipe(recipe2); // use chocolate 20, have 15
        assertEquals(200, coffeeMaker.makeCoffee(0, 200));
        assertEquals(coffeeMaker.checkInventory(), testInventory.toString());
    }

    /**
     * Test purchasing beverage with money less than the price of the drink
     */
    @Test
    public void testMakeNotEnoughMoneyCoffee() {
        Inventory testInventory = new Inventory();
        coffeeMaker.addRecipe(recipe3); // price = 100

        assertEquals(10, coffeeMaker.makeCoffee(0, 10));
        assertEquals(75, coffeeMaker.makeCoffee(0, 75));
        assertEquals(coffeeMaker.checkInventory(), testInventory.toString());
    }

    /**
     * Test adding the 4th recipe
     */
    @Test
    public void testAddFourthRecipe() {
        assertTrue(coffeeMaker.addRecipe(recipe1));
        assertTrue(coffeeMaker.addRecipe(recipe2));
        assertTrue(coffeeMaker.addRecipe(recipe3));
        assertFalse(coffeeMaker.addRecipe(recipe4));
    }

    /**
     * Test adding recipe with the existing name
     */
    @Test
    public void testAddSameRecipeName() {
        assertTrue(coffeeMaker.addRecipe(recipe1));
        recipe2.setName("Coffee");
        assertFalse(coffeeMaker.addRecipe(recipe2));
    }

    /**
     * Test deleting recipe swing steps
     */
    @Test
    public void testDeleteRecipeStep() {
        coffeeMaker.addRecipe(recipe1); // position 0
        coffeeMaker.addRecipe(recipe2); // position 1
        coffeeMaker.addRecipe(recipe3); // position 2
        assertEquals(coffeeMaker.deleteRecipe(1), "Mocha");
        assertEquals(coffeeMaker.deleteRecipe(0), "Coffee");
        assertEquals(coffeeMaker.deleteRecipe(2), "Latte");
    }

    /**
     * Test deleting recipe with number that is out of bounds of the number of recipes
     */
    @Test
    public void testDeleteOutOfBoundRecipe() {
        coffeeMaker.addRecipe(recipe1); // position 0
        coffeeMaker.addRecipe(recipe2); // position 1
        coffeeMaker.addRecipe(recipe3); // position 2
        assertNull(coffeeMaker.deleteRecipe(3));
    }

    /**
     * Test deleting an empty recipe
     */
    @Test
    public void testDeleteEmptyRecipe() {
        assertNull(coffeeMaker.deleteRecipe(0));
    }

    /**
     * Test editing recipe with number that is out of bounds of the number of recipes
     */
    @Test
    public void testEditEmptyRecipe() {
        assertNull(coffeeMaker.editRecipe(0, recipe1));
    }

    /**
     * Test editing an empty recipe
     */
    @Test
    public void testEditOutOfBoundRecipe() {
        coffeeMaker.addRecipe(recipe1); // position 0
        assertNull(coffeeMaker.editRecipe(1, recipe2));
    }

    /**
     * Test setting a recipe with invalid price
     *
     * @throws RecipeException when use invalid price in Recipe class
     */
    @Test(expected = RecipeException.class)
    public void testSetInvalidPriceRecipe() throws RecipeException {
        try {
            recipe1.setPrice("-100");
        } catch (RecipeException e) {
            try {
                recipe1.setPrice("100.50");
            } catch (RecipeException e2) {
                recipe1.setPrice("abc");
            }
        }
    }

    /**
     * Test setting a recipe with invalid unit of coffee
     *
     * @throws RecipeException when use invalid unit in Recipe class
     */
    @Test(expected = RecipeException.class)
    public void testSetInvalidCoffeeRecipe() throws RecipeException {
        try {
            recipe1.setAmtCoffee("-100");
        } catch (RecipeException e) {
            try {
                recipe1.setAmtCoffee("100.50");
            } catch (RecipeException e2) {
                recipe1.setAmtCoffee("abc");
            }
        }
    }

    /**
     * Test setting a recipe with invalid unit of sugar
     *
     * @throws RecipeException when use invalid unit in Recipe class
     */
    @Test(expected = RecipeException.class)
    public void testSetInvalidSugarRecipe() throws RecipeException {
        try {
            recipe1.setAmtSugar("-100");
        } catch (RecipeException e) {
            try {
                recipe1.setAmtSugar("100.50");
            } catch (RecipeException e2) {
                recipe1.setAmtSugar("abc");
            }
        }
    }

    /**
     * Test setting a recipe with invalid unit of milk
     *
     * @throws RecipeException when use invalid unit in Recipe class
     */
    @Test(expected = RecipeException.class)
    public void testSetInvalidMilkRecipe() throws RecipeException {
        try {
            recipe1.setAmtMilk("-100");
        } catch (RecipeException e) {
            try {
                recipe1.setAmtMilk("100.50");
            } catch (RecipeException e2) {
                recipe1.setAmtMilk("abc");
            }
        }
    }

    /**
     * Test setting a recipe with invalid unit of chocolate
     *
     * @throws RecipeException when use invalid unit in Recipe class
     */
    @Test(expected = RecipeException.class)
    public void testSetInvalidChocolateRecipe() throws RecipeException {
        try {
            recipe1.setAmtChocolate("-100");
        } catch (RecipeException e) {
            try {
                recipe1.setAmtChocolate("100.50");
            } catch (RecipeException e2) {
                recipe1.setAmtChocolate("abc");
            }
        }
    }

    /**
     * Test adding inventory with invalid unit of coffee
     *
     * @throws InventoryException when use invalid unit in Inventory class
     */
    @Test(expected = InventoryException.class)
    public void testAddInvalidCoffeeInventory() throws InventoryException {
        Inventory inventory = new Inventory();
        try {
            inventory.addCoffee("-100");
        } catch (InventoryException e) {
            try {
                inventory.addCoffee("100.50");
            } catch (InventoryException e2) {
                inventory.addCoffee("abc");
            }
        }
    }

    /**
     * Test adding inventory with invalid unit of sugar
     *
     * @throws InventoryException when use invalid unit in Inventory class
     */
    @Test(expected = InventoryException.class)
    public void testAddInvalidSugarInventory() throws InventoryException {
        Inventory inventory = new Inventory();
        try {
            inventory.addSugar("-100");
        } catch (InventoryException e) {
            try {
                inventory.addSugar("100.50");
            } catch (InventoryException e2) {
                inventory.addSugar("abc");
            }
        }
    }

    /**
     * Test adding inventory with invalid unit of milk
     *
     * @throws InventoryException when use invalid unit in Inventory class
     */
    @Test(expected = InventoryException.class)
    public void testAddInvalidMilkInventory() throws InventoryException {
        Inventory inventory = new Inventory();
        try {
            inventory.addMilk("-100");
        } catch (InventoryException e) {
            try {
                inventory.addMilk("100.50");
            } catch (InventoryException e2) {
                inventory.addMilk("abc");
            }
        }
    }

    /**
     * Test adding inventory with invalid unit of chocolate
     *
     * @throws InventoryException when use invalid unit in Inventory class
     */
    @Test(expected = InventoryException.class)
    public void testAddInvalidChocolateInventory() throws InventoryException {
        Inventory inventory = new Inventory();
        try {
            inventory.addChocolate("-100");
        } catch (InventoryException e) {
            try {
                inventory.addChocolate("100.50");
            } catch (InventoryException e2) {
                inventory.addChocolate("abc");
            }
        }
    }

    /**
     * Test using ingredients from a recipe
     */
    @Test
    public void testUseIngredients() {
        Inventory inventory = new Inventory();
        inventory.useIngredients(recipe4);
        assertEquals(inventory.getCoffee(), 12);
        assertEquals(inventory.getSugar(), 14);
        assertEquals(inventory.getMilk(), 14);
        assertEquals(inventory.getChocolate(), 11);
    }
}