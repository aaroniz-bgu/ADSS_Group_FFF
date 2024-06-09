package bgu.adss.fff.dev.util;

import bgu.adss.fff.dev.contracts.CategoryDto;

import java.io.PrintStream;
import java.util.Arrays;

public class CategoryUtil {

    public static void printCategoryNamesByTree(PrintStream out, CategoryDto category) {
        printCategoryNamesByTree(out, category, 0);
    }

    private static void printCategoryNamesByTree(PrintStream out, CategoryDto category, int depth) {
        for (int i = 0; i < depth; i++)
            out.print("\t");
        out.println("-" + category.categoryName());
        for (CategoryDto subCategory : category.children())
            printCategoryNamesByTree(out, subCategory, depth + 1);
    }

    public static void printCategoryNamesByLevel(PrintStream out, CategoryDto category) {
        out.println("Categories: ");
        for (int i = 1; i <= 3; i++) {
            String[] categoryNames = getCategoryNamesRecursive(category, i);
            out.println("\t Level " + i + ": " + Arrays.toString(categoryNames));
        }
    }

    private static String[] getCategoryNamesRecursive(CategoryDto category, int levelAccepted) {
        if (category.level() == levelAccepted)
            return new String[]{category.categoryName()};
        return Arrays.stream(category.children())
                .flatMap(subCategory -> Arrays.stream(getCategoryNamesRecursive(subCategory, levelAccepted)))
                .toArray(String[]::new);
    }

    public static void printProductsByCategoryTree(PrintStream out, CategoryDto category) {
        printProductsByCategoryTree(out, category, 0);
    }

    private static void printProductsByCategoryTree(PrintStream out, CategoryDto category, int depth) {
        String prefix = "\t".repeat(depth);
        out.println(prefix + ":" + category.categoryName());
        if (category.children().length == 0) {
            Arrays.stream(category.products()).forEach(product -> out.println(prefix + "\t- " + product.productName()));
        } else {
            for (CategoryDto subCategory : category.children())
                printProductsByCategoryTree(out, subCategory, depth + 1);
        }
    }
}
