import XCTest

/** The iOS twin of Android's emulator smoke test: the real app starts and shows its home screen. */
final class LaunchTest: XCTestCase {
    func testLaunchesToTheHomeScreen() throws {
        let app = XCUIApplication()
        app.launchArguments += ["-AppleLanguages", "(en)"]
        app.launch()
        // Compose names its texts and buttons by their text for the accessibility tree that UI tests read.
        let count = app.descendants(matching: .any).matching(NSPredicate(format: "label == %@", "Count")).firstMatch
        XCTAssertTrue(count.waitForExistence(timeout: 60))
        // Kept for the CI run's artifacts: what the home screen looks like on an iPhone.
        let shot = XCTAttachment(screenshot: app.screenshot())
        shot.name = "home"
        shot.lifetime = .keepAlways
        add(shot)
    }
}
