
# PR 1: Core
git checkout image-gen-pr1-core
sbt scalafmtAll
git add .
git commit -m "style: apply scalafmt formatting"
git push origin image-gen-pr1-core

# PR 2: Vertex AI
git checkout image-gen-pr2-vertexai
git merge image-gen-pr1-core -m "Merge branch 'image-gen-pr1-core' into image-gen-pr2-vertexai"
sbt scalafmtAll
git add .
git commit -m "style: apply scalafmt formatting"
git push origin image-gen-pr2-vertexai

# PR 3: Bedrock
git checkout image-gen-pr3-bedrock
git merge image-gen-pr1-core -m "Merge branch 'image-gen-pr1-core' into image-gen-pr3-bedrock"
sbt scalafmtAll
git add .
git commit -m "style: apply scalafmt formatting"
git push origin image-gen-pr3-bedrock

# PR 4: Stability AI
git checkout image-gen-pr4-stabilityai
git merge image-gen-pr1-core -m "Merge branch 'image-gen-pr1-core' into image-gen-pr4-stabilityai"
sbt scalafmtAll
git add .
git commit -m "style: apply scalafmt formatting"
git push origin image-gen-pr4-stabilityai

# PR 5: Fal AI
git checkout image-gen-pr5-falai
git merge image-gen-pr1-core -m "Merge branch 'image-gen-pr1-core' into image-gen-pr5-falai"
sbt scalafmtAll
git add .
git commit -m "style: apply scalafmt formatting"
git push origin image-gen-pr5-falai

# PR 6: Metrics
git checkout image-gen-pr6-metrics
git merge image-gen-pr1-core -m "Merge branch 'image-gen-pr1-core' into image-gen-pr6-metrics"
sbt scalafmtAll
git add .
git commit -m "style: apply scalafmt formatting"
git push origin image-gen-pr6-metrics

# Return to main
git checkout main
