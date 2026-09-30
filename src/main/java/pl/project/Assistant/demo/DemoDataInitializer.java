package pl.project.Assistant.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.project.Assistant.auth.User;
import pl.project.Assistant.auth.UserRepository;
import pl.project.Assistant.finance.category.Category;
import pl.project.Assistant.finance.category.CategoryRepository;
import pl.project.Assistant.finance.transaction.Transaction;
import pl.project.Assistant.finance.transaction.TransactionRepository;
import pl.project.Assistant.finance.transaction.TransactionType;
import pl.project.Assistant.kitchen.Nutrition;
import pl.project.Assistant.kitchen.fridge.FridgeService;
import pl.project.Assistant.kitchen.product.Product;
import pl.project.Assistant.kitchen.product.ProductService;
import pl.project.Assistant.kitchen.product.Unit;
import pl.project.Assistant.kitchen.recipe.Ingredient;
import pl.project.Assistant.kitchen.recipe.Recipe;
import pl.project.Assistant.kitchen.recipe.RecipeRepository;
import pl.project.Assistant.kitchen.shopping.ShoppingListService;
import pl.project.Assistant.task.Task;
import pl.project.Assistant.task.TaskRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Seeds a demo account with sample data when the "demo" profile is active.
 * Runs only once: if the demo user already exists, nothing is changed.
 */
@Component
@Profile("demo")
public class DemoDataInitializer implements CommandLineRunner {

    static final String DEMO_EMAIL = "demo@assistant.dev";
    static final String DEMO_PASSWORD = "demo1234";

    private static final Logger log = LoggerFactory.getLogger(DemoDataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final ProductService productService;
    private final FridgeService fridgeService;
    private final ShoppingListService shoppingListService;
    private final RecipeRepository recipeRepository;

    public DemoDataInitializer(UserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               TaskRepository taskRepository,
                               CategoryRepository categoryRepository,
                               TransactionRepository transactionRepository,
                               ProductService productService,
                               FridgeService fridgeService,
                               ShoppingListService shoppingListService,
                               RecipeRepository recipeRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.productService = productService;
        this.fridgeService = fridgeService;
        this.shoppingListService = shoppingListService;
        this.recipeRepository = recipeRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.findByEmail(DEMO_EMAIL).isPresent()) {
            log.info("Demo user {} already exists, skipping demo data", DEMO_EMAIL);
            return;
        }

        User user = new User();
        user.setEmail(DEMO_EMAIL);
        user.setPassword(passwordEncoder.encode(DEMO_PASSWORD));
        user.setRole(User.RoleEnum.USER);
        userRepository.save(user);

        seedTasks(user);
        seedFinance(user);
        seedKitchen(user);

        log.info("Demo data created, log in as {} / {}", DEMO_EMAIL, DEMO_PASSWORD);
    }

    private void seedTasks(User user) {
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        task(user, "Pay the electricity bill", "Due date is on the invoice", true, now.minusDays(3));
        task(user, "Book a dentist appointment", null, true, now.minusDays(1));
        task(user, "Get the car inspected", "Inspection station on Main Street", false, now.plusDays(2));
        task(user, "Prepare the Monday presentation", "Quarterly results", false, now.plusDays(4));
        task(user, "Return library books", null, false, now.plusWeeks(1));
        task(user, "Plan the vacation", "Compare accommodation prices", false, now.plusWeeks(3));
    }

    private void task(User user, String title, String description, boolean completed, LocalDateTime until) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setCompleted(completed);
        task.setUntil(until);
        task.setOwner(user);
        taskRepository.save(task);
    }

    private void seedFinance(User user) {
        Category food = category(user, "Food");
        Category transport = category(user, "Transport");
        Category salary = category(user, "Salary");
        Category bills = category(user, "Bills");
        Category fun = category(user, "Entertainment");

        LocalDate today = LocalDate.now();
        transaction(user, salary, TransactionType.INCOME, "Monthly salary", "7200.00", today.minusMonths(1).withDayOfMonth(1));
        transaction(user, salary, TransactionType.INCOME, "Monthly salary", "7200.00", today.withDayOfMonth(1));
        transaction(user, food, TransactionType.EXPENSE, "Grocery store", "184.37", today.minusDays(55));
        transaction(user, food, TransactionType.EXPENSE, "Supermarket", "96.12", today.minusDays(47));
        transaction(user, food, TransactionType.EXPENSE, "Lunch out", "68.00", today.minusDays(40));
        transaction(user, food, TransactionType.EXPENSE, "Weekly groceries", "231.45", today.minusDays(26));
        transaction(user, food, TransactionType.EXPENSE, "Bakery", "18.50", today.minusDays(12));
        transaction(user, food, TransactionType.EXPENSE, "Grocery store", "142.80", today.minusDays(4));
        transaction(user, transport, TransactionType.EXPENSE, "Monthly transit pass", "110.00", today.minusDays(52));
        transaction(user, transport, TransactionType.EXPENSE, "Fuel", "265.30", today.minusDays(33));
        transaction(user, transport, TransactionType.EXPENSE, "Monthly transit pass", "110.00", today.minusDays(22));
        transaction(user, bills, TransactionType.EXPENSE, "Rent", "1850.00", today.minusDays(50));
        transaction(user, bills, TransactionType.EXPENSE, "Electricity", "214.66", today.minusDays(35));
        transaction(user, bills, TransactionType.EXPENSE, "Rent", "1850.00", today.minusDays(20));
        transaction(user, bills, TransactionType.EXPENSE, "Internet", "59.99", today.minusDays(15));
        transaction(user, fun, TransactionType.EXPENSE, "Cinema", "54.00", today.minusDays(30));
        transaction(user, fun, TransactionType.EXPENSE, "Streaming subscription", "43.00", today.minusDays(8));
    }

    private Category category(User user, String name) {
        Category category = new Category();
        category.setName(name);
        category.setOwner(user);
        return categoryRepository.save(category);
    }

    private void transaction(User user, Category category, TransactionType type, String title, String amount, LocalDate date) {
        Transaction transaction = new Transaction();
        transaction.setTitle(title);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setType(type);
        transaction.setTransactionDate(date);
        transaction.setCategory(category);
        transaction.setOwner(user);
        transactionRepository.save(transaction);
    }

    private void seedKitchen(User user) {
        Map<String, Product> products = new HashMap<>();

        fridge(user, products, "Eggs", Unit.PCS, "10");
        fridge(user, products, "Milk", Unit.ML, "1000");
        fridge(user, products, "Butter", Unit.G, "200");
        fridge(user, products, "Wheat flour", Unit.G, "1000");
        fridge(user, products, "Sugar", Unit.G, "500");
        fridge(user, products, "Tomatoes", Unit.PCS, "6");
        fridge(user, products, "Onion", Unit.PCS, "3");
        fridge(user, products, "Garlic cloves", Unit.PCS, "5");
        fridge(user, products, "Spaghetti", Unit.G, "500");
        fridge(user, products, "Bacon", Unit.G, "100");
        fridge(user, products, "Parmesan", Unit.G, "150");
        fridge(user, products, "Arborio rice", Unit.G, "1000");
        fridge(user, products, "Bell pepper", Unit.PCS, "2");
        fridge(user, products, "Sour cream", Unit.ML, "200");
        fridge(user, products, "Potatoes", Unit.G, "2000");

        shopping(user, products, "Olive oil", Unit.ML, "500");
        shopping(user, products, "Bananas", Unit.PCS, "6");
        shopping(user, products, "Bread", Unit.PCS, "1");
        shopping(user, products, "Coffee", Unit.G, "250");

        // Every ingredient is in the fridge in sufficient amount -> READY
        recipe(user, products, "Pancakes", "Classic sweet pancakes", 4, 30, 520,
                List.of("Blend the flour, milk, eggs and sugar into a smooth batter.",
                        "Let the batter rest for 15 minutes.",
                        "Fry thin pancakes in hot butter on both sides."),
                List.of(ing("Wheat flour", Unit.G, "250"), ing("Milk", Unit.ML, "500"),
                        ing("Eggs", Unit.PCS, "2"), ing("Sugar", Unit.G, "20"), ing("Butter", Unit.G, "20")));

        // READY
        recipe(user, products, "Scrambled eggs with tomatoes", "A quick breakfast", 2, 10, 380,
                List.of("Sauté the chopped onion in butter until translucent.",
                        "Add the diced tomatoes and cook for 2 minutes.",
                        "Crack in the eggs and stir until set."),
                List.of(ing("Eggs", Unit.PCS, "4"), ing("Tomatoes", Unit.PCS, "2"),
                        ing("Butter", Unit.G, "15"), ing("Onion", Unit.PCS, "1")));

        // Bacon: 100 g in the fridge, 200 g needed -> INSUFFICIENT
        recipe(user, products, "Spaghetti carbonara", "Pasta with bacon, egg and cheese", 3, 25, 690,
                List.of("Cook the pasta al dente.",
                        "Fry the bacon in a dry pan.",
                        "Whisk the eggs with the grated cheese.",
                        "Toss the hot pasta with the bacon and egg mixture, stirring quickly."),
                List.of(ing("Spaghetti", Unit.G, "400"), ing("Bacon", Unit.G, "200"),
                        ing("Eggs", Unit.PCS, "3"), ing("Parmesan", Unit.G, "80")));

        // Bell pepper INSUFFICIENT (2 of 3), smoked sausage MISSING
        recipe(user, products, "Pepper and sausage stew", "A one-pot Hungarian classic", 4, 45, 450,
                List.of("Fry the sliced sausage with the onion and garlic.",
                        "Add the peppers and cook for 10 minutes.",
                        "Add the tomatoes and simmer covered for 20 minutes."),
                List.of(ing("Bell pepper", Unit.PCS, "3"), ing("Smoked sausage", Unit.G, "300"),
                        ing("Onion", Unit.PCS, "1"), ing("Tomatoes", Unit.PCS, "3"), ing("Garlic cloves", Unit.PCS, "2")));

        // Mushrooms and vegetable stock MISSING
        recipe(user, products, "Mushroom risotto", "Creamy risotto", 3, 40, 560,
                List.of("Sauté the onion in butter, add the rice and toast for a minute.",
                        "Add the stock gradually, stirring constantly.",
                        "Stir in the fried mushrooms and grated cheese."),
                List.of(ing("Arborio rice", Unit.G, "300"), ing("Mushrooms", Unit.G, "400"),
                        ing("Vegetable stock", Unit.ML, "1000"), ing("Onion", Unit.PCS, "1"),
                        ing("Butter", Unit.G, "30"), ing("Parmesan", Unit.G, "50")));
    }

    private Product product(User user, Map<String, Product> products, String name, Unit unit) {
        return products.computeIfAbsent(name, key -> productService.findOrCreate(key, unit, user));
    }

    private void fridge(User user, Map<String, Product> products, String name, Unit unit, String amount) {
        fridgeService.addOrIncrease(product(user, products, name, unit), new BigDecimal(amount), user);
    }

    private void shopping(User user, Map<String, Product> products, String name, Unit unit, String amount) {
        shoppingListService.addOrIncrease(product(user, products, name, unit), new BigDecimal(amount), user);
    }

    private record IngredientSpec(String name, Unit unit, String amount) {
    }

    private static IngredientSpec ing(String name, Unit unit, String amount) {
        return new IngredientSpec(name, unit, amount);
    }

    private void recipe(User user, Map<String, Product> products, String name, String description,
                        int servings, int prepTimeMinutes, int calories,
                        List<String> steps, List<IngredientSpec> ingredients) {
        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setDescription(description);
        recipe.setServings(servings);
        recipe.setPrepTimeMinutes(prepTimeMinutes);
        recipe.setNutrition(new Nutrition(calories, null, null, null));
        recipe.setOwner(user);
        recipe.getSteps().addAll(steps);
        for (IngredientSpec spec : ingredients) {
            Product product = product(user, products, spec.name(), spec.unit());
            recipe.addIngredient(new Ingredient(product, new BigDecimal(spec.amount())));
        }
        recipeRepository.save(recipe);
    }
}
