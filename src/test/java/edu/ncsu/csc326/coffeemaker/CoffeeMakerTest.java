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
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.Mockito;

import io.cucumber.java.en.*;

/**
 * Unit tests for CoffeeMaker class.
 *
 * @author Nanthakarn Limkool
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

    // test Mock objects
    @Mock
    private RecipeBook mockRecipeBook;
    private CoffeeMaker mockCoffeeMaker;

    // tolerance for comparing double values
    static final double TOL = 1.0E-4;
    Integer payment;

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

        //Set up for recipe1
        recipe1 = createRecipe("Coffee", 50, 3, 1, 1, 0);

        //Set up for recipe2
        recipe2 = createRecipe("Mocha", 75, 3, 1, 1, 20);

        //Set up for recipe3
        recipe3 = createRecipe("Latte", 100, 3, 3, 1, 0);

        //Set up for recipe4
        recipe4 = createRecipe("Hot Chocolate", 65, 3, 1, 1, 4);

        // inject mock objects for each @Mock attribute
        // MockitoAnnotations.initMocks(this);  // deprecated
        mockRecipeBook = Mockito.mock(RecipeBook.class);
        when(mockRecipeBook.getRecipes()).thenReturn(new Recipe[]{recipe1, recipe2, recipe3});
        mockCoffeeMaker = new CoffeeMaker(mockRecipeBook, new Inventory());
    }

    /**
     * Return a new Recipe
     *
     * @param name
     * @param price
     * @param amtCoffee
     * @param amtMilk
     * @param amtSugar
     * @param amtChocolate
     * @return new recipe
     * @throws RecipeException
     */
    private Recipe createRecipe(String name, int price, int amtCoffee, int amtMilk, int amtSugar,
                                int amtChocolate) throws RecipeException {
        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setPrice(String.valueOf(price));
        recipe.setAmtCoffee(String.valueOf(amtCoffee));
        recipe.setAmtMilk(String.valueOf(amtMilk));
        recipe.setAmtSugar(String.valueOf(amtSugar));
        recipe.setAmtChocolate(String.valueOf(amtChocolate));
        return recipe;
    }

    /**
     * Test recipeBook and inventory of new CoffeeMaker
     *
     * @TestCase A1
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
     *
     * @TestCase A2
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
     *
     * @TestCase A3
     */
    @Test
    public void testDeleteRecipe() {
        coffeeMaker.addRecipe(recipe1); // position 0
        coffeeMaker.deleteRecipe(0);
        assertNull(coffeeMaker.getRecipes()[0]);
    }

    /**
     * Test editing recipe in CoffeeMaker recipe book
     *
     * @TestCase A4
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
     * @TestCase A5
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
     * @TestCase A6
     */
    @Test
    public void testCheckInventory() throws InventoryException {
        assertEquals(coffeeMaker.checkInventory(), "Coffee: 15\nMilk: 15\nSugar: 15\nChocolate: 15\n");

        coffeeMaker.addInventory("10", "20", "30", "40");
        assertEquals(coffeeMaker.checkInventory(), "Coffee: 25\nMilk: 35\nSugar: 45\nChocolate: 55\n");
    }

    /**
     * Test purchasing beverage with enough money and ingredients
     *
     * @TestCase A7
     */
    @Test
    public void testMakeCoffee() {
        Inventory testInventory = new Inventory();

        assertEquals(25, mockCoffeeMaker.makeCoffee(0, 75));
        testInventory.useIngredients(recipe1);
        assertEquals(mockCoffeeMaker.checkInventory(), testInventory.toString());

        assertEquals(0, mockCoffeeMaker.makeCoffee(2, 100));
        testInventory.useIngredients(recipe3);
        assertEquals(mockCoffeeMaker.checkInventory(), testInventory.toString());
    }

    /**
     * Test purchasing an empty recipe beverage
     *
     * @TestCase A8
     */
    @Test
    public void testMakeEmptyRecipeCoffee() {
        Inventory testInventory = new Inventory();

        assertEquals(75, mockCoffeeMaker.makeCoffee(1, 75));
        assertEquals(100, mockCoffeeMaker.makeCoffee(3, 100));
        assertEquals(mockCoffeeMaker.checkInventory(), testInventory.toString());
    }

    /**
     * Test purchasing beverage with not enough ingredients in the inventory
     *
     * @TestCase A9
     */
    @Test
    public void testMakeNotEnoughIngredientsCoffee() {
        Inventory testInventory = new Inventory();

        assertEquals(200, mockCoffeeMaker.makeCoffee(1, 200));
        assertEquals(mockCoffeeMaker.checkInventory(), testInventory.toString());
    }

    /**
     * Test purchasing beverage with money less than the price of the drink
     *
     * @TestCase A10
     */
    @Test
    public void testMakeNotEnoughMoneyCoffee() {
        Inventory testInventory = new Inventory();

        assertEquals(10, mockCoffeeMaker.makeCoffee(2, 10));
        assertEquals(75, mockCoffeeMaker.makeCoffee(2, 75));
        assertEquals(coffeeMaker.checkInventory(), testInventory.toString());
    }

    /**
     * Test adding the 4th recipe
     *
     * @TestCase B1
     */
    @Test
    public void testAddFourthRecipe() {
        assertFalse(mockCoffeeMaker.addRecipe(recipe4));
    }

    /**
     * Test adding recipe with the existing name
     *
     * @TestCase B2
     */
    @Test
    public void testAddSameRecipeName() {
        when(mockRecipeBook.getRecipes()).thenReturn(new Recipe[]{recipe1});
        recipe2.setName("Coffee");
        assertFalse(mockCoffeeMaker.addRecipe(recipe2));
    }

    /**
     * Test deleting recipe swing steps
     *
     * @TestCase B3
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
     * Test deleting an empty recipe
     *
     * @TestCase B4
     */
    @Test
    public void testDeleteEmptyRecipe() {
        assertNull(coffeeMaker.deleteRecipe(0));
    }

    /**
     * Test deleting recipe with number that is out of bounds of the number of recipes
     *
     * @TestCase B5
     */
    @Test
    public void testDeleteOutOfBoundRecipe() {
        coffeeMaker.addRecipe(recipe1); // position 0
        assertNull(coffeeMaker.deleteRecipe(1));
    }

    /**
     * Test editing recipe with number that is out of bounds of the number of recipes
     *
     * @TestCase B6
     */
    @Test
    public void testEditEmptyRecipe() {
        assertNull(coffeeMaker.editRecipe(0, recipe1));
    }

    /**
     * Test editing an empty recipe
     *
     * @TestCase B7
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
     * @TestCase C1
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
     * @TestCase C2
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
     * @TestCase C3
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
     * @TestCase C4
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
     * @TestCase C5
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
     * @TestCase D1
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
     * @TestCase D2
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
     * @TestCase D3
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
     * @TestCase D4
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
     *
     * @TestCase D5
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

    /**
     * Test if enoughIngredients() return correctly
     *
     * @TestCase D6
     */
    @Test
    public void testCheckEnoughIngredients() throws RecipeException {
        Inventory inventory = new Inventory();
        Recipe recipeA = createRecipe("recipeA", 100, 10, 10, 10, 10);
        Recipe recipeB = createRecipe("recipeB", 100, 15, 15, 15, 15);
        Recipe recipeC = createRecipe("recipeC", 100, 20, 15, 15, 15);
        Recipe recipeD = createRecipe("recipeD", 100, 15, 20, 15, 15);
        Recipe recipeE = createRecipe("recipeE", 100, 15, 15, 20, 15);
        Recipe recipeF = createRecipe("recipeF", 100, 15, 15, 15, 20);

        assertTrue(inventory.enoughIngredients(recipeA));
        assertTrue(inventory.enoughIngredients(recipeB));
        assertFalse(inventory.enoughIngredients(recipeC));
        assertFalse(inventory.enoughIngredients(recipeD));
        assertFalse(inventory.enoughIngredients(recipeE));
        assertFalse(inventory.enoughIngredients(recipeF));
    }

    /**
     * Initializes a recipe to test using cucumber
     *
     * @throws RecipeException if there was an error parsing the ingredient
     *                         amount when setting up the recipe.
     */
    @io.cucumber.java.Before
    public void setUpScenario() throws RecipeException {
        coffeeMaker = new CoffeeMaker();
        //Set up for recipe1
        recipe1 = createRecipe("Coffee", 50, 3, 1, 1, 0);
    }

    /**
     * Set coffee price
     *
     * @param price
     * @throws RecipeException if there was an error parsing the ingredient
     *                         amount when setting up the recipe.
     */
    @Given("I order coffee that cost {int}")
    public void iOrderCoffeeThatCost(Integer price) throws RecipeException {
        recipe1.setPrice(price.toString());
    }

    /**
     * Set testing payment
     *
     * @param amount
     */
    @When("I pay {int}")
    public void iPay(Integer amount) {
        payment = amount;
    }

    /**
     * Check returned change of payment
     *
     * @param paymentChange
     */
    @Then("I get back {int}")
    public void iGetBack(Integer paymentChange) {
        coffeeMaker.addRecipe(recipe1);
        assertEquals(paymentChange, coffeeMaker.makeCoffee(0, payment), TOL);
    }

    /**
     * Set amount of ingredient to make not enough ingredient situation
     *
     * @throws RecipeException if there was an error parsing the ingredient
     *                         amount when setting up the recipe.
     */
    @When("I don't have enough ingredients")
    public void iDontHaveEnoughIngredients() throws RecipeException {
        recipe1.setAmtCoffee("1000");
    }
}