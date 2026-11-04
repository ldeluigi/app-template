import ComposeApp
import SwiftUI

/** The iOS app: one screen, the shared Compose UI. */
@main
struct App: SwiftUI.App {
    var body: some Scene {
        WindowGroup {
            SharedScreen().ignoresSafeArea()
        }
    }
}

/** Hosts the screen the shared Kotlin code makes. */
struct SharedScreen: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ controller: UIViewController, context: Context) {}
}
