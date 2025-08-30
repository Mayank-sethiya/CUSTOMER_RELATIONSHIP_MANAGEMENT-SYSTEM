##WORKFLOW UNDERSTANDING##


├── .gitattributes
├── .github
    └── workflows
    │   └── static.yml
├── .gitignore
├── .mvn
    └── wrapper
    │   └── maven-wrapper.properties
├── mvnw
├── mvnw.cmd
├── pom.xml
└── src
    ├── main
        ├── java
        │   └── com
        │   │   └── CustomerRelationshipManagement
        │   │       ├── CustomerRelationshipManagementApplication.java
        │   │       ├── config
        │   │           └── SecurityConfig.java
        │   │       ├── controller
        │   │           ├── AdminController.java
        │   │           ├── AiReportController.java
        │   │           ├── BroadcastController.java
        │   │           ├── ContactController.java
        │   │           ├── TaskController.java
        │   │           ├── TaskReportController.java
        │   │           └── UserController.java
        │   │       ├── entity
        │   │           ├── AdminEntity.java
        │   │           ├── BroadcastEntity.java
        │   │           ├── ContactEntity.java
        │   │           ├── TaskEntity.java
        │   │           ├── TaskReportEntity.java
        │   │           └── UserEntity.java
        │   │       ├── repository
        │   │           ├── AdminRepository.java
        │   │           ├── BroadcastRepository.java
        │   │           ├── ContactRepository.java
        │   │           ├── FileStorageService.java
        │   │           ├── MonthlySignup.java
        │   │           ├── TaskReportRepository.java
        │   │           ├── TaskRepository.java
        │   │           └── UserRepository.java
        │   │       └── service
        │   │           ├── AdminService.java
        │   │           ├── BroadcastService.java
        │   │           ├── ContactService.java
        │   │           ├── TaskService.java
        │   │           └── UserService.java
        └── resources
        │   ├── application.properties
        │   └── static
        │       ├── admin
        │           ├── Leads.html
        │           ├── Task-Assign.html
        │           ├── View-Reports.html
        │           ├── admin_login.html
        │           ├── animations
        │           │   ├── user-welcome-animation.json
        │           │   └── welcome-animation.json
        │           ├── broadcast-messaging.html
        │           ├── contact-management.html
        │           ├── dashboard.html
        │           ├── js
        │           │   ├── lottie-player.js
        │           │   └── particles.min.js
        │           └── user-management.html
        │       ├── config.js
        │       ├── index.html
        │       └── user
        │           ├── notification.html
        │           ├── register.html
        │           ├── user-controlpanel.html
        │           ├── user-login.html
        │           └── view-tasks.html
    └── test
        └── java
            └── com
                └── CustomerRelationshipManagement
                    └── CustomerRelationshipManagementApplicationTests.java


/.gitattributes:
--------------------------------------------------------------------------------
1 | /mvnw text eol=lf
2 | *.cmd text eol=crlf
3 | 


--------------------------------------------------------------------------------
/.github/workflows/static.yml:
--------------------------------------------------------------------------------
 1 | # Simple workflow for deploying static content to GitHub Pages
 2 | name: Deploy static content to Pages
 3 | 
 4 | on:
 5 |   # Runs on pushes targeting the default branch
 6 |   push:
 7 |     branches: ["master"]
 8 | 
 9 |   # Allows you to run this workflow manually from the Actions tab
10 |   workflow_dispatch:
11 | 
12 | # Sets permissions of the GITHUB_TOKEN to allow deployment to GitHub Pages
13 | permissions:
14 |   contents: read
15 |   pages: write
16 |   id-token: write
17 | 
18 | # Allow only one concurrent deployment, skipping runs queued between the run in-progress and latest queued.
19 | # However, do NOT cancel in-progress runs as we want to allow these production deployments to complete.
20 | concurrency:
21 |   group: "pages"
22 |   cancel-in-progress: false
23 | 
24 | jobs:
25 |   # Single deploy job since we're just deploying
26 |   deploy:
27 |     environment:
28 |       name: github-pages
29 |       url: ${{ steps.deployment.outputs.page_url }}
30 |     runs-on: ubuntu-latest
31 |     steps:
32 |       - name: Checkout
33 |         uses: actions/checkout@v4
34 |       - name: Setup Pages
35 |         uses: actions/configure-pages@v5
36 |       - name: Upload artifact
37 |         uses: actions/upload-pages-artifact@v3
38 |         with:
39 |           # Upload entire repository
40 |           path: '.'
41 |       - name: Deploy to GitHub Pages
42 |         id: deployment
43 |         uses: actions/deploy-pages@v4
44 | 


--------------------------------------------------------------------------------
/.gitignore:
--------------------------------------------------------------------------------
 1 | HELP.md
 2 | target/
 3 | .mvn/wrapper/maven-wrapper.jar
 4 | !**/src/main/**/target/
 5 | !**/src/test/**/target/
 6 | 
 7 | ### STS ###
 8 | .apt_generated
 9 | .classpath
10 | .factorypath
11 | .project
12 | .settings
13 | .springBeans
14 | .sts4-cache
15 | 
16 | ### IntelliJ IDEA ###
17 | .idea
18 | *.iws
19 | *.iml
20 | *.ipr
21 | 
22 | ### NetBeans ###
23 | /nbproject/private/
24 | /nbbuild/
25 | /dist/
26 | /nbdist/
27 | /.nb-gradle/
28 | build/
29 | !**/src/main/**/build/
30 | !**/src/test/**/build/
31 | 
32 | ### VS Code ###
33 | .vscode/
34 | 

