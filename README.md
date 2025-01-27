# Getting Started

## Description

This is a demo project to outline the issue when updating a child association using the `PATCH` and `PUT` methods provided by Spring Data REST.

The tests are conducted in the class: `src/test/java/com.noratt.springdatarestrelationshipissueexample.EntityRelationshipUpdateTests`. The tests have been annotated with `@Order` to ensure the requests
are carried out in the correct order and data is in place before carrying out the next test. Test summary and results are provided below.

* Initialise the data by persisting 2 `School` entities directly through the @Autowired Repository.


* Test 1: `shouldCreateParentEntityWithoutAnyChildren_Using_POST` - `POST` request to create a parent entity with no children.
  * Expect: Parent entity created with an `id` of 1 and no children.
  * Actual: Parent entity created with an `id` of 1 and no children.
  * Result: Pass


* Test 2: `shouldAddTwoChildren_Using_PUT` - `PUT` request to add 2 child entities to the parent.
  * Expect: Two child entities added with `id`'s 1 and 2. Correct nested school entities.
  * Actual: Two child entities added with `id`'s 1 and 2. Correct nested school entities.
  * Result: Pass
    

* Test 3: `shouldUpdateSchoolForSecondChild_Using_PUT` - `PUT` request to update the school of the second child.
  * Expect: School for 2nd child has been updated with an `id` of 1.
  * Actual: School has not been updated for 2nd child. Still old school with `id` of 2.
  * Result: Fail


* Test 4: `shouldRemoveFirstChild_Using_PUT` - `PUT` request to remove the first child entity.
  * Expect: One child with `id` of 2. Correct nested school entity with `id` of 2.
  * Actual: One child with `id` of 2. Nested school entity is incorrect with `id` of 1.
  * Result: Fail


* Test 5: `shouldClearChildrenArray_Using_PUT` - `PUT` request to clear the child array.
  * Expect: Child array has been cleared.
  * Actual: Child array has been cleared.
  * Result: Pass


* Test 6: `shouldAddTwoChildren_Using_PATCH` - `PATCH` request to add 2 child entities to the parent entity.
  * Expect: Two child entities added with `id`'s 1 and 2. Correct nested school entities.
  * Actual: Two child entities added with `id`'s 1 and 2. Correct nested school entities.
  * Result: Pass


* Test 7: `shouldUpdateSchoolForSecondChild_Using_PATCH` - `PATCH` request to update the school of the second child.
  * Expect: School for 2nd child has been updated with an `id` of 1.
  * Actual: School has not been updated for 2nd child. Still old school with `id` of 2.
  * Result: Fail


* Test 8: `shouldRemoveFirstChild_Using_PATCH` - `PATCH` request to remove the first child entity.
  * Expect: One child with `id` of 2. Correct nested school entity with `id` of 2.
  * Actual: One child with `id` of 2. Nested school entity is incorrect with `id` of 1.
  * Result: Fail


* Test 9: `shouldClearChildrenArray_Using_PATCH` - `PATCH` request to clear the child array.
  * Expect: Child array has been cleared.
  * Actual: Child array has been cleared.
  * Result: Pass