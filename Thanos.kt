fun main(args: Array<String>) {
    JDABuilder().setToken(<token>)
        .addEventListener(ThanosBot())
        .build()
}

class ThanosBot : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.member.isOwner &&
            event.message.contentDisplay == "https://cdn.discordapp.com/attachments/521639180036472844/521639202958344202/iu.png"
        ) {

            val guildController = GuildController(event.guild)

            val membersID = mutableListOf<String>()
            for (member in event.guild.members.shuffled()) {
                membersID.add(member.user.id)
            }

            val members = SettingsManager
                .load(Settings(membersID.subList(0, membersID.size / 2)))
                .members

            println("Number of users to be banned: ${members.size}.")
            //println("Press enter to begin ban."); readLine()

            println("Beginning ban...")
            println("-----------------------------------------")
            for (memberID in members) {
                try {

                    guildController.ban(memberID, 0, "You have been balanced by Thanos.")
                        .complete()
                    println("Banned ${event.jda.getUserById(memberID).name} with ID $memberID.")

                } catch (e: InsufficientPermissionException) {
                    println(
                        "Failed to ban ${event.jda.getUserById(memberID).name} with ID $memberID. " +
                                "Error: Insufficient Permission exception. "
                    )
                } catch (e: HierarchyException) {
                    println(
                        "Failed to ban ${event.jda.getUserById(memberID).name} with ID $memberID. " +
                                "Error: Hierarchy exception. "
                    )
                } catch (e: IllegalStateException) {
                    println("Failed to ban ID $memberID. Error: The provided member is null!")
                }
            }
            println("-----------------------------------------")
            println("Operation complete.")

            event.channel.sendMessage(
                "Perfectly balanced, as all things should be. But what did it cost?"
            ).complete()
        } else if (event.author.id == "437154938527678465" &&
            event.message.contentDisplay == ";info"
        ) {
            println("Press enter to receive info. "); readLine()
            val thanosRole = event.guild.getRoleById("523355744129515532")
            for (role in event.guild.roles) {
                if (role.position < thanosRole.position) {
                    event.channel.sendMessage("Role ${role.name} is included in ban list.").complete()
                }
            }
        }
    }
}
